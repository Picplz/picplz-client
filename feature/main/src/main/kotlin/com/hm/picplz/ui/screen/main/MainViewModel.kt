package com.hm.picplz.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.GetPhotographerPortfoliosUseCase
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

private const val HOME_FEED_LIMIT = 5
private const val NEARBY_DISTANCE_METERS = 2_000L

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
        private val getCurrentMemberIdUseCase: GetCurrentMemberIdUseCase,
        private val updateMemberLocationUseCase: UpdateMemberLocationUseCase,
        private val getNearbyPhotographersUseCase: GetNearbyPhotographersUseCase,
        private val getPhotographerPortfoliosUseCase: GetPhotographerPortfoliosUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MainState.idle())
        val state: StateFlow<MainState> = _state.asStateFlow()

        private val _sideEffect = Channel<MainSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

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

                MainIntent.SearchClicked -> {
                    sendSideEffect(MainSideEffect.NavigateToSearch)
                }

                is MainIntent.PhotographerClicked -> {
                    sendSideEffect(MainSideEffect.NavigateToPhotographerDetail(intent.photographerId))
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

                getNearbyPhotographersUseCase(
                    longitude = location.longitude,
                    latitude = location.latitude,
                    distance = NEARBY_DISTANCE_METERS,
                ).onSuccess { filtered ->
                    val photographers = (filtered.active + filtered.inactive).take(HOME_FEED_LIMIT)
                    val homeItems = photographers.toHomeItems()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            homeItems = homeItems,
                        )
                    }
                }.onFailure {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = MainLoadError.NearbyPhotographers,
                            homeItems = emptyList(),
                        )
                    }
                }
            }
        }

        private suspend fun List<Photographer>.toHomeItems(): List<CustomerHomeItem> =
            coroutineScope {
                map { photographer ->
                    async { photographer.toHomeItem() }
                }.map { it.await() }
            }

        private suspend fun Photographer.toHomeItem(): CustomerHomeItem {
            val portfolio =
                getPhotographerPortfoliosUseCase(
                    photographerId = id,
                    page = 0,
                    size = 1,
                ).getOrNull()?.firstOrNull()
            val locationText = portfolio?.location ?: activeAreas.firstOrNull().orEmpty()
            return CustomerHomeItem(
                photographerId = id,
                photographerName = name,
                profileImageUri = profileImageUri,
                portfolioImageUri = portfolio?.representativeImage,
                location = locationText,
                uploadDate = portfolio?.uploadDate,
                photoCount = portfolio?.photoCount ?: 0,
                isActive = isActive,
                distance = distance,
                moodTags = photoMoods,
            )
        }
    }
