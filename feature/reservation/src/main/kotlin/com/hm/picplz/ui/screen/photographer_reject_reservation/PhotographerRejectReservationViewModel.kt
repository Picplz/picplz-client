package com.hm.picplz.ui.screen.photographer_reject_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.PhotographerRejectReservation
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
    ) : ViewModel() {
        private val orderId: String = savedStateHandle.toRoute<PhotographerRejectReservation>().orderId

        private val _state = MutableStateFlow(PhotographerRejectReservationState.idle(orderId))
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
                    // TODO: 거절 사유 전송 API 연동
                    emitSideEffect(PhotographerRejectReservationSideEffect.NavigateToChat)
                }

                PhotographerRejectReservationIntent.OnBackClick -> {
                    emitSideEffect(PhotographerRejectReservationSideEffect.NavigateBack)
                }
            }
        }

        private fun emitSideEffect(sideEffect: PhotographerRejectReservationSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
