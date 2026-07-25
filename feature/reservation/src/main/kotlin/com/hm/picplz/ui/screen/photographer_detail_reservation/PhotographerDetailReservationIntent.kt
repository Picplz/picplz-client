package com.hm.picplz.ui.screen.photographer_detail_reservation

sealed interface PhotographerDetailReservationIntent {
    data object NavigateToChat : PhotographerDetailReservationIntent

    data object ApproveReservation : PhotographerDetailReservationIntent

    data object ConfirmReservation : PhotographerDetailReservationIntent

    /** "예약 취소" 버튼 → 작가 취소 사유 입력 플로우로 이동 */
    data object NavigateToCancelReservation : PhotographerDetailReservationIntent

    data object NavigateBack : PhotographerDetailReservationIntent
}
