package com.hm.picplz.ui.screen.photographer_reject_reservation

sealed interface PhotographerRejectReservationIntent {
    /** 거절 사유 선택 (단일 선택) */
    data class SelectReason(val reason: PhotographerRejectReason) : PhotographerRejectReservationIntent

    /** 직접 입력 텍스트 변경 */
    data class UpdateDirectInput(val text: String) : PhotographerRejectReservationIntent

    /** 하단 "다음" 버튼 → 확인 다이얼로그 노출 */
    data object OnNextClick : PhotographerRejectReservationIntent

    /** 확인 다이얼로그 "확인" → 거절 확정 */
    data object OnConfirmDialogConfirm : PhotographerRejectReservationIntent

    /** 확인 다이얼로그 "취소" · 바깥 영역 터치 */
    data object OnConfirmDialogDismiss : PhotographerRejectReservationIntent

    /** 상단 뒤로가기 */
    data object OnBackClick : PhotographerRejectReservationIntent
}
