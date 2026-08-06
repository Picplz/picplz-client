package com.hm.picplz.ui.screen.write_review

sealed interface WriteReviewIntent {
    data class SelectRating(val rating: ReviewRating) : WriteReviewIntent

    data class UpdateNegativeFeedback(val text: String) : WriteReviewIntent

    /** 별점 선택(1/2)의 "별점 등록" */
    data object OnRatingSubmitClick : WriteReviewIntent

    data object OnBackClick : WriteReviewIntent
}
