package com.hm.picplz.ui.screen.write_review

data class WriteReviewState(
    val orderId: String = "",
    val currentStep: Step = Step.RATING,
    val customerNickname: String = "",
    val photographerName: String = "",
    val selectedRating: ReviewRating? = null,
    /** 1~2점 선택 시 받는 "어떤점이 아쉬웠나요?" 입력값 (선택사항) */
    val negativeFeedbackText: String = "",
) {
    /** 별점 선택(1/2) 단계의 "별점 등록" 활성화 조건 */
    fun isRatingStepValid(): Boolean = selectedRating != null

    /** 부정 평가(1~2점)일 때만 아쉬운 점 입력란을 노출합니다. */
    fun showNegativeFeedback(): Boolean = selectedRating?.needsNegativeFeedback == true

    enum class Step { RATING, CONTENT }

    companion object {
        fun idle(orderId: String): WriteReviewState = WriteReviewState(orderId = orderId)
    }
}
