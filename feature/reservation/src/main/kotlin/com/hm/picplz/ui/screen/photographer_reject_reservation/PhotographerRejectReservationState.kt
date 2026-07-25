package com.hm.picplz.ui.screen.photographer_reject_reservation

data class PhotographerRejectReservationState(
    val orderId: String = "",
    val selectedReason: PhotographerRejectReason? = null,
    val directInputText: String = "",
    val showConfirmDialog: Boolean = false,
) {
    /** 하단 "다음" 버튼 활성화 조건 */
    fun isNextEnabled(): Boolean =
        when (selectedReason) {
            null -> false
            PhotographerRejectReason.DIRECT_INPUT -> directInputText.isNotBlank()
            else -> true
        }

    companion object {
        fun idle(orderId: String): PhotographerRejectReservationState =
            PhotographerRejectReservationState(orderId = orderId)
    }
}
