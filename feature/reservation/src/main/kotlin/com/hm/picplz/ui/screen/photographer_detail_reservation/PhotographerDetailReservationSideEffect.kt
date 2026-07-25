package com.hm.picplz.ui.screen.photographer_detail_reservation

sealed interface PhotographerDetailReservationSideEffect {
    data object NavigateToPrev : PhotographerDetailReservationSideEffect

    data class NavigateToCancelReservation(val orderId: String) : PhotographerDetailReservationSideEffect
}
