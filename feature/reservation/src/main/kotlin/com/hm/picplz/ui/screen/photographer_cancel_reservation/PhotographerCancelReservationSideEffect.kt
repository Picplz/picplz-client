package com.hm.picplz.ui.screen.photographer_cancel_reservation

sealed interface PhotographerCancelReservationSideEffect {
    data object NavigateBack : PhotographerCancelReservationSideEffect

    data object NavigateToCancelConfirm : PhotographerCancelReservationSideEffect
}
