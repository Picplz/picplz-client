package com.hm.picplz.ui.screen.detail_reservation

sealed interface DetailReservationSideEffect {
    data object NavigateToPrev : DetailReservationSideEffect

    data object NavigateToCancelReservation : DetailReservationSideEffect

    data object NavigateToOrderDetail : DetailReservationSideEffect

    data object NavigateToWriteReview : DetailReservationSideEffect
}
