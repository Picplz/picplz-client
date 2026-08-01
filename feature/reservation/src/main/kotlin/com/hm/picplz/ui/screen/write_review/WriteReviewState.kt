package com.hm.picplz.ui.screen.write_review

/** 촬영 경험 입력 최소/최대 글자 수 */
const val REVIEW_CONTENT_MIN_LENGTH = 10
const val REVIEW_CONTENT_MAX_LENGTH = 600

data class WriteReviewState(
    val orderId: String = "",
    val currentStep: Step = Step.RATING,
    val customerNickname: String = "",
    val photographerName: String = "",
    val selectedRating: ReviewRating? = null,
    /** 1~2점 선택 시 받는 "어떤점이 아쉬웠나요?" 입력값 (선택사항) */
    val negativeFeedbackText: String = "",
    /** "촬영 경험을 들려주세요" 입력값 (필수) */
    val contentText: String = "",
    val showExitDialog: Boolean = false,
    /**
     * 마지막으로 띄운 토스트 문구. 퇴장 애니메이션이 끝날 때까지 문구가 남아 있어야 하므로
     * [showToast]가 false가 되어도 지우지 않습니다.
     */
    val toastMessageResId: Int? = null,
    val showToast: Boolean = false,
) {
    /** 별점 선택(1/2) 단계의 "별점 등록" 활성화 조건 */
    fun isRatingStepValid(): Boolean = selectedRating != null

    /** 부정 평가(1~2점)일 때만 아쉬운 점 입력란을 노출합니다. */
    fun showNegativeFeedback(): Boolean = selectedRating?.needsNegativeFeedback == true

    /** 촬영 경험(2/2) 단계의 "리뷰 등록" 활성화 조건 */
    fun isContentStepValid(): Boolean = contentText.length >= REVIEW_CONTENT_MIN_LENGTH

    enum class Step { RATING, CONTENT }

    companion object {
        fun idle(orderId: String): WriteReviewState = WriteReviewState(orderId = orderId)
    }
}
