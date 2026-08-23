package com.hm.picplz.ui.screen.photographer_cancel_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.data.service.ReservationService
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.navigation.model.PhotographerCancelReservation
import com.hm.picplz.ui.screen.model.toServerReasons
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

private const val CANCEL_REASON_MAX_LENGTH = 100

@HiltViewModel
class PhotographerCancelReservationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val reservationService: ReservationService,
    ) : ViewModel() {
        private val reservationId: Long =
            savedStateHandle.toRoute<PhotographerCancelReservation>().reservationId

        private val _state = MutableStateFlow(PhotographerCancelReservationState.idle(reservationId))
        val state: StateFlow<PhotographerCancelReservationState> = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<PhotographerCancelReservationSideEffect>()
        val sideEffect: SharedFlow<PhotographerCancelReservationSideEffect> = _sideEffect.asSharedFlow()

        fun handleIntent(intent: PhotographerCancelReservationIntent) {
            when (intent) {
                is PhotographerCancelReservationIntent.ToggleReason -> {
                    _state.update { currentState ->
                        val newReasons = currentState.selectedReasons.toMutableSet()
                        if (!newReasons.remove(intent.reason)) {
                            newReasons.add(intent.reason)
                        }
                        currentState.copy(selectedReasons = newReasons)
                    }
                }

                is PhotographerCancelReservationIntent.UpdateDirectInput -> {
                    _state.update { it.copy(directInputText = intent.text.take(CANCEL_REASON_MAX_LENGTH)) }
                }

                is PhotographerCancelReservationIntent.SetAgreedWithCustomer -> {
                    _state.update { it.copy(agreedWithCustomer = intent.agreed) }
                }

                is PhotographerCancelReservationIntent.SetAgreedToPolicy -> {
                    _state.update { it.copy(agreedToPolicy = intent.agreed) }
                }

                PhotographerCancelReservationIntent.OnNextClick -> {
                    _state.update { it.copy(currentStep = PhotographerCancelReservationState.Step.POLICY) }
                }

                PhotographerCancelReservationIntent.OnSubmitClick -> {
                    _state.update { it.copy(showConfirmDialog = true) }
                }

                PhotographerCancelReservationIntent.OnConfirmDialogDismiss -> {
                    _state.update { it.copy(showConfirmDialog = false) }
                }

                PhotographerCancelReservationIntent.OnConfirmDialogConfirm -> {
                    _state.update { it.copy(showConfirmDialog = false) }
                    submitCancel()
                }

                PhotographerCancelReservationIntent.OnToastDismiss -> {
                    _state.update { it.copy(showToast = false) }
                }

                PhotographerCancelReservationIntent.OnBackClick -> {
                    val currentStep = _state.value.currentStep
                    if (currentStep == PhotographerCancelReservationState.Step.POLICY) {
                        _state.update { it.copy(currentStep = PhotographerCancelReservationState.Step.REASON) }
                    } else {
                        emitSideEffect(PhotographerCancelReservationSideEffect.NavigateBack)
                    }
                }
            }
        }

        /**
         * 화면의 "고객과 사전 합의" 체크(`agreedWithCustomer`)는 서버 요청에 담을 필드가 없어
         * 아직 전송하지 못합니다. 백엔드에 필드 추가를 요청해 둔 상태입니다.
         */
        private fun submitCancel() {
            val current = _state.value
            if (current.isLoading) return

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                reservationService
                    .cancelReservation(
                        reservationId = current.reservationId,
                        reasons = current.selectedReasons.toServerReasons(),
                        reasonDetail = current.directInputText,
                    ).onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        emitSideEffect(PhotographerCancelReservationSideEffect.NavigateToCancelConfirm)
                    }.onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                toastMessageResId = R.string.reservation_error_cancel_failed,
                                showToast = true,
                            )
                        }
                    }
            }
        }

        private fun emitSideEffect(sideEffect: PhotographerCancelReservationSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
