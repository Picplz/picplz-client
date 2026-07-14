package com.hm.picplz.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.UpdateMemberLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSearchViewModel
    @Inject
    constructor(
        private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
        private val getCurrentMemberIdUseCase: GetCurrentMemberIdUseCase,
        private val updateMemberLocationUseCase: UpdateMemberLocationUseCase,
        private val getNearbyPhotographersUseCase: GetNearbyPhotographersUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MainSearchState.idle())
        val state: StateFlow<MainSearchState> = _state.asStateFlow()

        fun handleIntent(intent: MainSearchIntent) {
            _state.update { MainSearchReducer.reduce(it, intent) }

            if (intent is MainSearchIntent.SearchSubmitted && intent.query.isNotBlank()) {
                loadCurrentNearbyPhotographers()
            }
        }

        private fun loadCurrentNearbyPhotographers() {
            getCurrentLocationUseCase(
                onLocationReceived = ::loadNearbyPhotographers,
                onPermissionDenied = {
                    _state.update { MainSearchReducer.reduce(it, MainSearchIntent.SearchLoadFailed) }
                },
            )
        }

        private fun loadNearbyPhotographers(location: LocationCoordinate) {
            viewModelScope.launch {
                val memberId = getCurrentMemberIdUseCase()
                if (memberId == null) {
                    _state.update { MainSearchReducer.reduce(it, MainSearchIntent.SearchLoadFailed) }
                    return@launch
                }

                updateMemberLocationUseCase(
                    memberId = memberId,
                    location = location,
                )

                getNearbyPhotographersUseCase(
                    longitude = location.longitude,
                    latitude = location.latitude,
                    distance = 2_000L,
                ).onSuccess { filtered ->
                    _state.update {
                        MainSearchReducer.reduce(
                            it,
                            MainSearchIntent.NearbyPhotographersLoaded(filtered.toSearchItems()),
                        )
                    }
                }.onFailure {
                    _state.update { MainSearchReducer.reduce(it, MainSearchIntent.SearchLoadFailed) }
                }
            }
        }

        private fun FilteredPhotographers.toSearchItems(): List<MainSearchPhotographerItem> =
            (active + inactive).map { it.toSearchItem() }

        private fun Photographer.toSearchItem(): MainSearchPhotographerItem =
            MainSearchPhotographerItem(
                id = id.toString(),
                name = name,
                profileImageUri = profileImageUri,
                areaSummary = activeAreas.joinToString(", "),
                isAvailableNow = isActive,
                moodTags = photoMoods,
                distance = distance,
            )
    }
