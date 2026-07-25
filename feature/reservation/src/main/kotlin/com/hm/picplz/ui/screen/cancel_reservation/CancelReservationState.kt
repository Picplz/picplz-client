package com.hm.picplz.ui.screen.cancel_reservation

data class CancelReservationState(
    val orderId: String = "",
    val selectedReasons: Set<CancelReason> = emptySet(),
    val directInputText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    fun isSubmitButtonEnabled(): Boolean =
        selectedReasons.isNotEmpty() && (
            selectedReasons.any { it != CancelReason.DIRECT_INPUT } || // 직접 입력 이외의 사유가 있거나
                directInputText.isNotBlank() // 직접 입력 텍스트가 있으면 OK
        )

    companion object {
        fun idle(orderId: String): CancelReservationState = CancelReservationState(orderId = orderId)
    }
}
