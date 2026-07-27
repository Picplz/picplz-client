package com.hm.picplz.ui.screen.photographer_detail_reservation

sealed interface PhotographerDetailReservationSideEffect {
    data object NavigateToPrev : PhotographerDetailReservationSideEffect

    /** "예약 거절" → 거절 사유 입력 화면으로 이동 */
    data class NavigateToRejectReason(val orderId: String) : PhotographerDetailReservationSideEffect

    data class NavigateToCancelReservation(val orderId: String) : PhotographerDetailReservationSideEffect
}
