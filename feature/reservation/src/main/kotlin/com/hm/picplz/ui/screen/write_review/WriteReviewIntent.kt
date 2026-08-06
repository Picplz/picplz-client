package com.hm.picplz.ui.screen.write_review

sealed interface WriteReviewIntent {
    data class SelectRating(val rating: ReviewRating) : WriteReviewIntent

    data class UpdateNegativeFeedback(val text: String) : WriteReviewIntent

    data class UpdateContent(val text: String) : WriteReviewIntent

    /** 별점 선택(1/2)의 "별점 등록" */
    data object OnRatingSubmitClick : WriteReviewIntent

    /** 촬영 경험(2/2)의 "리뷰 등록" */
    data object OnReviewSubmitClick : WriteReviewIntent

    data object OnBackClick : WriteReviewIntent

    data object OnExitDialogConfirm : WriteReviewIntent

    data object OnExitDialogDismiss : WriteReviewIntent

    data object DismissToast : WriteReviewIntent
}
