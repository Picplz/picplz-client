package com.hm.picplz.ui.screen.photographer_reject_reservation

data class PhotographerRejectReservationState(
    val reservationId: Long = 0L,
    val selectedReason: PhotographerRejectReason? = null,
    val directInputText: String = "",
    val showConfirmDialog: Boolean = false,
    val isLoading: Boolean = false,
    /**
     * 마지막으로 띄운 토스트 문구. 퇴장 애니메이션이 끝날 때까지 남아 있어야 하므로
     * [showToast]가 false가 되어도 지우지 않습니다.
     */
    val toastMessageResId: Int? = null,
    val showToast: Boolean = false,
) {
    /** 하단 "다음" 버튼 활성화 조건 */
    fun isNextEnabled(): Boolean =
        !isLoading &&
            when (selectedReason) {
                null -> false
                PhotographerRejectReason.DIRECT_INPUT -> directInputText.isNotBlank()
                else -> true
            }

    companion object {
        fun idle(reservationId: Long): PhotographerRejectReservationState =
            PhotographerRejectReservationState(reservationId = reservationId)
    }
}
