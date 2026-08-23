package com.hm.picplz.ui.screen.photographer_detail_reservation

sealed interface PhotographerDetailReservationIntent {
    data object NavigateToChat : PhotographerDetailReservationIntent

    data object ApproveReservation : PhotographerDetailReservationIntent

    data object ConfirmReservation : PhotographerDetailReservationIntent

    /** 예약 승인 대기중 상태의 "예약 거절" 버튼 → 거절 사유 입력 화면으로 이동 */
    data object RejectReservation : PhotographerDetailReservationIntent

    /** "예약 취소" 버튼 → 작가 취소 사유 입력 플로우로 이동 */
    data object NavigateToCancelReservation : PhotographerDetailReservationIntent

    data object NavigateBack : PhotographerDetailReservationIntent

    /** 실패 토스트 표시 시간이 끝남 */
    data object OnToastDismiss : PhotographerDetailReservationIntent
}
