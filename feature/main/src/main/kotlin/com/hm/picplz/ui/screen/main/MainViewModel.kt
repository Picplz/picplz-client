package com.hm.picplz.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.GetPhotographerActiveAreasUseCase
import com.hm.picplz.domain.usecase.GetPhotographerPortfoliosUseCase
import com.hm.picplz.domain.usecase.GetPortfolioUseCase
import com.hm.picplz.domain.usecase.UpdateMemberLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val HOME_FEED_PAGE_SIZE = 5
private const val NEARBY_DISTANCE_METERS = 2_000L

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
        private val getCurrentMemberIdUseCase: GetCurrentMemberIdUseCase,
        private val updateMemberLocationUseCase: UpdateMemberLocationUseCase,
        private val getNearbyPhotographersUseCase: GetNearbyPhotographersUseCase,
        private val getPhotographerActiveAreasUseCase: GetPhotographerActiveAreasUseCase,
        private val getPhotographerPortfoliosUseCase: GetPhotographerPortfoliosUseCase,
        private val getPortfolioUseCase: GetPortfolioUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MainState.idle())
        val state: StateFlow<MainState> = _state.asStateFlow()

        private val _sideEffect = Channel<MainSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()
        private var nearbyPhotographers = emptyList<Photographer>()
        private val portfolioRequestsInFlight = mutableSetOf<Long>()
        private val resolvedPhotographerIds = mutableSetOf<Long>()

        fun handleIntent(intent: MainIntent) {
            when (intent) {
                is MainIntent.EnterScreen -> {
                    _state.update { it.copy(locationPermissionGranted = intent.hasLocationPermission) }
                    if (intent.hasLocationPermission && state.value.homeItems.isEmpty()) {
                        loadCurrentLocation()
                    }
                }

                MainIntent.RequestLocationPermission -> {
                    _state.update { it.copy(hasRequestedPermission = true) }
                    viewModelScope.launch {
                        _sideEffect.send(MainSideEffect.RequestLocationPermission)
                    }
                }

                is MainIntent.LocationPermissionResult -> {
                    _state.update {
                        it.copy(
                            locationPermissionGranted = intent.granted,
                            hasRequestedPermission = true,
                            errorMessage = null,
                        )
                    }
                    if (intent.granted) {
                        loadCurrentLocation()
                    }
                }

                MainIntent.RetryLoad -> {
                    if (state.value.locationPermissionGranted) {
                        loadCurrentLocation()
                    } else {
                        handleIntent(MainIntent.RequestLocationPermission)
                    }
                }

                MainIntent.LoadNextPage -> loadNextPage()

                is MainIntent.PortfolioVisible -> loadPortfolioDetail(intent.photographerId)

                MainIntent.SearchClicked -> {
                    sendSideEffect(MainSideEffect.NavigateToSearch)
                }

                is MainIntent.PhotographerClicked -> {
                    sendSideEffect(MainSideEffect.NavigateToPhotographerDetail(intent.photographerId))
                }

                MainIntent.ReportClicked -> {
                    sendSideEffect(MainSideEffect.ShowReportUnavailable)
                }

                MainIntent.DevEntryClicked -> {
                    sendSideEffect(MainSideEffect.NavigateToDev)
                }

                is MainIntent.RegionSelected -> {
                    _state.update { it.copy(selectedRegion = intent.region) }
                }
            }
        }

        private fun sendSideEffect(sideEffect: MainSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(sideEffect)
            }
        }

        private fun loadCurrentLocation() {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getCurrentLocationUseCase(
                onLocationReceived = ::loadHomeFeed,
                onPermissionDenied = {
                    _state.update {
                        it.copy(
                            locationPermissionGranted = false,
                            hasRequestedPermission = true,
                            isLoading = false,
                            errorMessage = MainLoadError.LocationUnavailable,
                        )
                    }
                },
            )
        }

        private fun loadHomeFeed(location: LocationCoordinate) {
            viewModelScope.launch {
                val memberId = getCurrentMemberIdUseCase()
                if (memberId == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = MainLoadError.NearbyPhotographers,
                            homeItems = emptyList(),
                        )
                    }
                    return@launch
                }

                updateMemberLocationUseCase(
                    memberId = memberId,
                    location = location,
                )

                loadNearbyPhotographers(location)
            }
        }

        private fun loadNextPage() {
            val currentState = state.value
            if (currentState.isLoading ||
                currentState.isLoadingMore ||
                !currentState.hasNextPage
            ) {
                return
            }

            val nextItems =
                nearbyPhotographers
                    .drop(currentState.homeItems.size)
                    .take(HOME_FEED_PAGE_SIZE)
                    .map { it.toHomeItem() }
            val mergedItems = currentState.homeItems + nextItems
            _state.update {
                it.copy(
                    isLoadingMore = false,
                    hasNextPage = mergedItems.size < nearbyPhotographers.size,
                    nextPage = currentState.nextPage + 1,
                    loadMoreFailed = false,
                    homeItems = mergedItems,
                )
            }
        }

        private suspend fun loadNearbyPhotographers(location: LocationCoordinate) {
            portfolioRequestsInFlight.clear()
            resolvedPhotographerIds.clear()
            getNearbyPhotographersUseCase(
                longitude = location.longitude,
                latitude = location.latitude,
                distance = NEARBY_DISTANCE_METERS,
            ).onSuccess { filtered ->
                nearbyPhotographers =
                    (filtered.active + filtered.inactive)
                        .distinctBy(Photographer::id)
                val initialItems =
                    nearbyPhotographers
                        .take(HOME_FEED_PAGE_SIZE)
                        .map { it.toHomeItem() }
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        hasNextPage = initialItems.size < nearbyPhotographers.size,
                        nextPage = 1,
                        loadMoreFailed = false,
                        errorMessage = null,
                        homeItems = initialItems,
                    )
                }
            }.onFailure {
                nearbyPhotographers = emptyList()
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        hasNextPage = false,
                        loadMoreFailed = false,
                        errorMessage = MainLoadError.NearbyPhotographers,
                        homeItems = emptyList(),
                    )
                }
            }
        }

        private fun Photographer.toHomeItem(): CustomerHomeItem =
            CustomerHomeItem(
                photographerId = id,
                photographerName = name.removeSuffix(" 작가").trim(),
                profileImageUri = profileImageUri,
                activeArea = activeAreas.toAreaNamesText(),
                portfolioId = null,
                portfolioImageUris = emptyList(),
                location = "",
                uploadDate = null,
                photoCount = 0,
                isActive = isActive,
            )

        private fun loadPortfolioDetail(photographerId: Long) {
            if (state.value.homeItems.none { it.photographerId == photographerId }) {
                return
            }
            if (photographerId in portfolioRequestsInFlight ||
                photographerId in resolvedPhotographerIds
            ) {
                return
            }

            portfolioRequestsInFlight += photographerId
            viewModelScope.launch {
                val (activeAreas, portfolioSummary) =
                    coroutineScope {
                        val activeAreasDeferred =
                            async {
                                getPhotographerActiveAreasUseCase(photographerId)
                                    .getOrNull()
                                    .orEmpty()
                            }
                        val portfolioDeferred =
                            async {
                                getPhotographerPortfoliosUseCase(
                                    photographerId = photographerId,
                                    page = 0,
                                    size = 1,
                                ).getOrNull()?.firstOrNull()
                            }
                        activeAreasDeferred.await() to portfolioDeferred.await()
                    }
                val activeArea = activeAreas.toActivityAreaText()
                if (activeArea.isNotEmpty()) {
                    _state.update { current ->
                        current.copy(
                            homeItems =
                                current.homeItems.map { homeItem ->
                                    if (homeItem.photographerId == photographerId) {
                                        homeItem.copy(activeArea = activeArea)
                                    } else {
                                        homeItem
                                    }
                                },
                        )
                    }
                }
                if (portfolioSummary != null) {
                    _state.update { current ->
                        current.copy(
                            homeItems =
                                current.homeItems.map { homeItem ->
                                    if (homeItem.photographerId == photographerId) {
                                        homeItem.copy(
                                            portfolioId = portfolioSummary.id.takeIf { it > 0L },
                                            portfolioImageUris = listOfNotNull(portfolioSummary.representativeImage),
                                            location = portfolioSummary.location ?: homeItem.location,
                                            uploadDate = portfolioSummary.uploadDate,
                                            photoCount = portfolioSummary.photoCount,
                                        )
                                    } else {
                                        homeItem
                                    }
                                },
                        )
                    }
                }

                val portfolio =
                    portfolioSummary
                        ?.takeIf { it.id > 0L && it.photoCount > 1 }
                        ?.let { getPortfolioUseCase(it.id).getOrNull() }
                if (portfolio != null) {
                    _state.update { current ->
                        current.copy(
                            homeItems =
                                current.homeItems.map { homeItem ->
                                    if (homeItem.photographerId == photographerId) {
                                        homeItem.copy(
                                            portfolioImageUris =
                                                portfolio.imageUris.takeIf { it.isNotEmpty() }
                                                    ?: homeItem.portfolioImageUris,
                                            location = portfolio.location ?: homeItem.location,
                                            uploadDate = portfolio.uploadDate ?: homeItem.uploadDate,
                                        )
                                    } else {
                                        homeItem
                                    }
                                },
                        )
                    }
                }
                portfolioRequestsInFlight -= photographerId
                resolvedPhotographerIds += photographerId
            }
        }
    }

private fun List<Area>.toActivityAreaText(): String = map(Area::displayName).toAreaNamesText()

private fun List<String>.toAreaNamesText(): String =
    map { area ->
        area
            .split(" ")
            .firstOrNull { part -> part.endsWith("구") || part.endsWith("군") }
            ?: area
    }.distinct()
        .take(2)
        .joinToString(" · ")
