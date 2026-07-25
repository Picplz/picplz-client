package com.hm.picplz.ui.screen.cancel_reservation

sealed interface CancelReservationIntent {
    data class ToggleReason(val reason: CancelReason) : CancelReservationIntent

    data class UpdateDirectInput(val text: String) : CancelReservationIntent

    data object OnBackClick : CancelReservationIntent

    data object OnSubmitClick : CancelReservationIntent
}
