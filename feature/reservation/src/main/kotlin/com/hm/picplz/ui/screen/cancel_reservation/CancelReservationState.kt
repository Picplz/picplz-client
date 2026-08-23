package com.hm.picplz.ui.screen.cancel_reservation

data class CancelReservationState(
    val reservationId: Long = 0L,
    val selectedReasons: Set<CancelReason> = emptySet(),
    val directInputText: String = "",
    val isLoading: Boolean = false,
    /**
     * 마지막으로 띄운 토스트 문구. 퇴장 애니메이션이 끝날 때까지 남아 있어야 하므로
     * [showToast]가 false가 되어도 지우지 않습니다.
     */
    val toastMessageResId: Int? = null,
    val showToast: Boolean = false,
) {
    fun isSubmitButtonEnabled(): Boolean =
        !isLoading &&
            selectedReasons.isNotEmpty() && (
                selectedReasons.any { it != CancelReason.DIRECT_INPUT } || // 직접 입력 이외의 사유가 있거나
                    directInputText.isNotBlank() // 직접 입력 텍스트가 있으면 OK
            )

    companion object {
        fun idle(reservationId: Long): CancelReservationState = CancelReservationState(reservationId = reservationId)
    }
}
