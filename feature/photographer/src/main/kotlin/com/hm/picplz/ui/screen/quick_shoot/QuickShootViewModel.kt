package com.hm.picplz.ui.screen.quick_shoot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.KakaoMapService
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.GetPhotographerDetailUseCase
import com.hm.picplz.domain.usecase.GetPhotographerPortfoliosUseCase
import com.hm.picplz.domain.usecase.GetPortfolioUseCase
import com.hm.picplz.ui.screen.quick_shoot.handler.LocationHandler
import com.hm.picplz.ui.screen.quick_shoot.handler.PhotographerSearchHandler
import com.hm.picplz.ui.screen.quick_shoot.util.OffsetGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickShootViewModel
    @Inject
    constructor(
        private val getNearbyPhotographersUseCase: GetNearbyPhotographersUseCase,
        private val getPhotographerDetailUseCase: GetPhotographerDetailUseCase,
        private val getPhotographerPortfoliosUseCase: GetPhotographerPortfoliosUseCase,
        private val getPortfolioUseCase: GetPortfolioUseCase,
        private val locationService: LocationService,
        private val kakaoMapService: KakaoMapService,
        private val tokenManager: TokenManager,
    ) : ViewModel() {
        private val _state = MutableStateFlow(QuickShootState.idle())
        val state: StateFlow<QuickShootState> get() = _state

        private val _sideEffect = Channel<QuickShootSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private val locationHandler = LocationHandler()
        private val offsetGenerator = OffsetGenerator()
        private val photographerSearchHandler = PhotographerSearchHandler(offsetGenerator)

        init {
            _state.update {
                it.copy(hasRequestedPermission = tokenManager.hasRequestedLocationPermission())
            }
        }

        fun handleIntent(intent: QuickShootIntent) {
            when (intent) {
                is QuickShootIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(QuickShootSideEffect.NavigateToPrev)
                    }
                }

                is QuickShootIntent.GetAddress -> {
                    viewModelScope.launch {
                        kakaoMapService.getAddressFromCoordinates(intent.coords)
                            .onSuccess { address ->
                                handleIntent(QuickShootIntent.SetAddress(address))
                            }
                            .onFailure { error ->
                                Log.w("QuickShoot", "주소 조회 실패", error)
                            }
                    }
                }

                is QuickShootIntent.RequestLocationPermission -> {
                    tokenManager.setHasRequestedLocationPermission()
                    _state.update { it.copy(hasRequestedPermission = true) }
                    viewModelScope.launch {
                        _sideEffect.send(QuickShootSideEffect.RequestLocationPermission)
                    }
                }

                is QuickShootIntent.SetLocationPermissionGranted -> {
                    _state.update { it.copy(locationPermissionGranted = intent.granted) }
                }

                is QuickShootIntent.GetCurrentLocation -> {
                    _state.update { it.copy(isFetchingGPS = true) }
                    locationService.getCurrentLocation(
                        onLocationReceived = { location ->
                            _state.update { it.copy(isFetchingGPS = false) }
                            handleIntent(QuickShootIntent.SetCurrentLocation(location))
                        },
                        onPermissionDenied = {
                            _state.update { it.copy(isFetchingGPS = false) }
                            handleIntent(QuickShootIntent.RequestLocationPermission)
                        },
                    )
                }

                is QuickShootIntent.FetchNearbyPhotographers,
                is QuickShootIntent.RefetchNearbyPhotographers,
                -> {
                    if (_state.value.isSearchingPhotographer) return
                    val userLocation = _state.value.userLocation
                    if (userLocation == null) {
                        Log.w("QuickShoot", "User location not available yet")
                        return
                    }
                    handleIntent(QuickShootIntent.SetSelectedPhotographerId(null))
                    handleIntent(QuickShootIntent.SetIsSearchingPhotographer(true))
                    handleIntent(QuickShootIntent.SetNearbyPhotographerLoadFailed(false))
                    viewModelScope.launch {
                        getNearbyPhotographersUseCase(
                            longitude = userLocation.longitude,
                            latitude = userLocation.latitude,
                            distance = QUICK_SHOOT_RADIUS_METERS,
                        )
                            .onSuccess { nearbyPhotographers ->
                                handleIntent(QuickShootIntent.SetIsSearchingPhotographer(false))
                                handleIntent(QuickShootIntent.SetNearbyPhotographerLoadFailed(false))
                                handleIntent(QuickShootIntent.SetNearbyPhotographers(nearbyPhotographers))
                                handleIntent(QuickShootIntent.DistributeRandomOffsets(nearbyPhotographers))
                            }
                            .onFailure { error ->
                                handleIntent(QuickShootIntent.SetIsSearchingPhotographer(false))
                                handleIntent(QuickShootIntent.SetNearbyPhotographerLoadFailed(true))
                                Log.e("FetchPhotographers", "작가 목록 로딩 실패", error)
                            }
                    }
                }

                is QuickShootIntent.SetSelectedPhotographerId -> {
                    selectPhotographer(intent.photographerId)
                }

                else -> {
                    val newState =
                        locationHandler.process(intent, _state.value)
                            ?: photographerSearchHandler.process(intent, _state.value)

                    newState?.let { _state.value = it }
                }
            }
        }

        private fun selectPhotographer(photographerId: Long?) {
            val currentState = _state.value
            val shouldClearSelection =
                photographerId == null || currentState.selectedPhotographerId == photographerId
            if (shouldClearSelection) {
                _state.update {
                    it.copy(
                        selectedPhotographerId = null,
                        selectedPhotographerPreview = null,
                        isLoadingSelectedPhotographer = false,
                    )
                }
                return
            }
            val selectedPhotographerId = photographerId ?: return

            val nearbyPhotographer =
                (currentState.nearbyPhotographers.active + currentState.nearbyPhotographers.inactive)
                    .find { it.id == selectedPhotographerId }
                    ?: return

            _state.update {
                it.copy(
                    selectedPhotographerId = selectedPhotographerId,
                    selectedPhotographerPreview = null,
                    isLoadingSelectedPhotographer = true,
                )
            }

            viewModelScope.launch {
                val detailResult = async { getPhotographerDetailUseCase(selectedPhotographerId) }
                val portfolioPhotos =
                    async {
                        val firstPortfolio =
                            getPhotographerPortfoliosUseCase(
                                photographerId = selectedPhotographerId,
                                size = 1,
                            ).getOrElse {
                                return@async emptyList()
                            }.firstOrNull() ?: return@async emptyList()

                        getPortfolioUseCase(firstPortfolio.id)
                            .getOrElse {
                                return@async emptyList()
                            }
                            .imageUris
                            .take(PHOTOGRAPHER_PREVIEW_PHOTO_COUNT)
                    }
                delay(PHOTOGRAPHER_PREVIEW_MINIMUM_LOADING_MILLIS)
                detailResult.await()
                    .onSuccess { detail ->
                        updateSelectedPhotographerPreview(
                            photographerId = selectedPhotographerId,
                            photographer =
                                nearbyPhotographer.copy(
                                    name = detail.profileInfo.name,
                                    profileImageUri = detail.profileInfo.profileImageUri,
                                    isActive = detail.profileInfo.isActive,
                                    photoMoods = detail.profileInfo.keyword,
                                    activeAreas = detail.profileInfo.workingArea,
                                    instagram = detail.profileInfo.socialAccount,
                                    equipment = detail.profileInfo.equipment,
                                    portfolioPhotos = portfolioPhotos.await(),
                                ),
                        )
                    }
                    .onFailure { error ->
                        Log.w("QuickShoot", "작가 프리뷰 로딩 실패", error)
                        updateSelectedPhotographerPreview(
                            photographerId = selectedPhotographerId,
                            photographer = nearbyPhotographer,
                        )
                    }
            }
        }

        private fun updateSelectedPhotographerPreview(
            photographerId: Long,
            photographer: Photographer,
        ) {
            _state.update { state ->
                if (state.selectedPhotographerId != photographerId) {
                    state
                } else {
                    state.copy(
                        selectedPhotographerPreview = photographer,
                        isLoadingSelectedPhotographer = false,
                    )
                }
            }
        }

        override fun onCleared() {
            super.onCleared()
            locationService.cleanup()
        }
    }

private const val QUICK_SHOOT_RADIUS_METERS = 2_000L
private const val PHOTOGRAPHER_PREVIEW_MINIMUM_LOADING_MILLIS = 1_500L
private const val PHOTOGRAPHER_PREVIEW_PHOTO_COUNT = 3
