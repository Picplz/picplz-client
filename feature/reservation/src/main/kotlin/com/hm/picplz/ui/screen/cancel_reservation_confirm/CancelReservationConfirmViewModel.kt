package com.hm.picplz.ui.screen.cancel_reservation_confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelReservationConfirmViewModel
    @Inject
    constructor() : ViewModel() {
        private val _sideEffect = MutableSharedFlow<CancelReservationConfirmSideEffect>()
        val sideEffect: SharedFlow<CancelReservationConfirmSideEffect> = _sideEffect.asSharedFlow()

        fun handleIntent(intent: CancelReservationConfirmIntent) {
            when (intent) {
                is CancelReservationConfirmIntent.NavigateToHistory -> {
                    emitSideEffect(CancelReservationConfirmSideEffect.NavigateToHistory)
                }

                is CancelReservationConfirmIntent.NavigateToHome -> {
                    emitSideEffect(CancelReservationConfirmSideEffect.NavigateToHome)
                }

                is CancelReservationConfirmIntent.NavigateBack -> {
                    emitSideEffect(CancelReservationConfirmSideEffect.NavigateToPrev)
                }
            }
        }

        private fun emitSideEffect(sideEffect: CancelReservationConfirmSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
