package com.hm.picplz.ui.screen.photographer_cancel_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.PhotographerCancelReservation
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
    ) : ViewModel() {
        private val orderId: String = savedStateHandle.toRoute<PhotographerCancelReservation>().orderId

        private val _state = MutableStateFlow(PhotographerCancelReservationState.idle(orderId))
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
                    // TODO: 취소 사유·사전 합의 여부 전송 API 연동
                    emitSideEffect(PhotographerCancelReservationSideEffect.NavigateToCancelConfirm)
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

        private fun emitSideEffect(sideEffect: PhotographerCancelReservationSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
