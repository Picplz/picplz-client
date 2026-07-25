package com.hm.picplz.ui.screen.photographer_cancel_reservation

sealed interface PhotographerCancelReservationIntent {
    data class ToggleReason(val reason: PhotographerCancelReason) : PhotographerCancelReservationIntent

    data class UpdateDirectInput(val text: String) : PhotographerCancelReservationIntent

    data class SetAgreedWithCustomer(val agreed: Boolean) : PhotographerCancelReservationIntent

    data class SetAgreedToPolicy(val agreed: Boolean) : PhotographerCancelReservationIntent

    /** 사유 입력(1/2) → 취소 규정(2/2) */
    data object OnNextClick : PhotographerCancelReservationIntent

    /** 취소 규정(2/2)의 "예약 취소" 버튼 → 확인 다이얼로그 노출 */
    data object OnSubmitClick : PhotographerCancelReservationIntent

    /** 확인 다이얼로그 "취소할게요" */
    data object OnConfirmDialogConfirm : PhotographerCancelReservationIntent

    /** 확인 다이얼로그 "아니오" · 바깥 영역 터치 */
    data object OnConfirmDialogDismiss : PhotographerCancelReservationIntent

    /** 상단 뒤로가기 (2단계면 1단계로, 1단계면 화면 종료) */
    data object OnBackClick : PhotographerCancelReservationIntent
}
