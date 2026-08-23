package com.hm.picplz.ui.screen.photographer_reject_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.data.service.ReservationService
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.navigation.model.PhotographerRejectReservation
import com.hm.picplz.ui.screen.model.toServerReason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotographerRejectReservationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val reservationService: ReservationService,
    ) : ViewModel() {
        private val reservationId: Long =
            savedStateHandle.toRoute<PhotographerRejectReservation>().reservationId

        private val _state = MutableStateFlow(PhotographerRejectReservationState.idle(reservationId))
        val state: StateFlow<PhotographerRejectReservationState> = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<PhotographerRejectReservationSideEffect>()
        val sideEffect: SharedFlow<PhotographerRejectReservationSideEffect> = _sideEffect.asSharedFlow()

        fun handleIntent(intent: PhotographerRejectReservationIntent) {
            when (intent) {
                is PhotographerRejectReservationIntent.SelectReason -> {
                    _state.update { it.copy(selectedReason = intent.reason) }
                }

                is PhotographerRejectReservationIntent.UpdateDirectInput -> {
                    _state.update { it.copy(directInputText = intent.text.take(REJECT_REASON_MAX_LENGTH)) }
                }

                PhotographerRejectReservationIntent.OnNextClick -> {
                    _state.update { it.copy(showConfirmDialog = true) }
                }

                PhotographerRejectReservationIntent.OnConfirmDialogDismiss -> {
                    _state.update { it.copy(showConfirmDialog = false) }
                }

                PhotographerRejectReservationIntent.OnConfirmDialogConfirm -> {
                    _state.update { it.copy(showConfirmDialog = false) }
                    submitReject()
                }

                PhotographerRejectReservationIntent.OnBackClick -> {
                    emitSideEffect(PhotographerRejectReservationSideEffect.NavigateBack)
                }

                PhotographerRejectReservationIntent.OnToastDismiss -> {
                    _state.update { it.copy(showToast = false) }
                }
            }
        }

        private fun submitReject() {
            val current = _state.value
            if (current.isLoading) return

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                reservationService
                    .rejectReservation(
                        reservationId = current.reservationId,
                        reasons = listOfNotNull(current.selectedReason?.toServerReason()),
                        reasonDetail = current.directInputText,
                    ).onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        emitSideEffect(PhotographerRejectReservationSideEffect.NavigateToChat)
                    }.onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                toastMessageResId = R.string.reservation_error_reject_failed,
                                showToast = true,
                            )
                        }
                    }
            }
        }

        private fun emitSideEffect(sideEffect: PhotographerRejectReservationSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
