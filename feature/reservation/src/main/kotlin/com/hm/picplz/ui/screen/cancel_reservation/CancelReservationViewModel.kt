package com.hm.picplz.ui.screen.cancel_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.CancelReservation
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
class CancelReservationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val orderId: String = savedStateHandle.toRoute<CancelReservation>().orderId

        private val _state = MutableStateFlow(CancelReservationState.idle(orderId))
        val state: StateFlow<CancelReservationState> = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<CancelReservationSideEffect>()
        val sideEffect: SharedFlow<CancelReservationSideEffect> = _sideEffect.asSharedFlow()

        fun handleIntent(intent: CancelReservationIntent) {
            when (intent) {
                is CancelReservationIntent.ToggleReason -> {
                    _state.update { currentState ->
                        val newReasons = currentState.selectedReasons.toMutableSet()
                        if (!newReasons.remove(intent.reason)) {
                            newReasons.add(intent.reason)
                        }
                        currentState.copy(selectedReasons = newReasons)
                    }
                }

                is CancelReservationIntent.UpdateDirectInput -> {
                    _state.update { it.copy(directInputText = intent.text.take(CANCEL_REASON_MAX_LENGTH)) }
                }

                CancelReservationIntent.OnBackClick -> {
                    emitSideEffect(CancelReservationSideEffect.NavigateBack)
                }

                CancelReservationIntent.OnSubmitClick -> {
                    // TODO: 취소 사유 전송 API 연동
                    emitSideEffect(CancelReservationSideEffect.NavigateToCancelReservationConfirm)
                }
            }
        }

        private fun emitSideEffect(sideEffect: CancelReservationSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
