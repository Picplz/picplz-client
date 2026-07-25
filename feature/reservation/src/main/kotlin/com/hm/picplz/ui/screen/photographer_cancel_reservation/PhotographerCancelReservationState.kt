package com.hm.picplz.ui.screen.photographer_cancel_reservation

data class PhotographerCancelReservationState(
    val orderId: String = "",
    val currentStep: Step = Step.REASON,
    val selectedReasons: Set<PhotographerCancelReason> = emptySet(),
    val directInputText: String = "",
    /** 1페이지 하단 "고객과 취소에 대해 사전 합의를 진행했어요." 체크 여부 */
    val agreedWithCustomer: Boolean = false,
    /** 2페이지 하단 "위 내용을 모두 확인하였으며, 이에 동의합니다." 체크 여부 */
    val agreedToPolicy: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val isLoading: Boolean = false,
) {
    /** 사유 입력(1/2) 단계의 "다음" 활성화 조건 */
    fun isReasonStepValid(): Boolean =
        selectedReasons.isNotEmpty() && (
            selectedReasons.any { it != PhotographerCancelReason.DIRECT_INPUT } || // 직접 입력 외 사유가 있거나
                directInputText.isNotBlank() // 직접 입력 텍스트가 있으면 OK
        )

    /** 취소 규정(2/2) 단계의 "예약 취소" 활성화 조건 */
    fun isPolicyStepValid(): Boolean = agreedToPolicy

    enum class Step { REASON, POLICY }

    companion object {
        fun idle(orderId: String): PhotographerCancelReservationState =
            PhotographerCancelReservationState(orderId = orderId)
    }
}
