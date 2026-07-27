package com.hm.picplz.ui.screen.write_review

sealed interface WriteReviewSideEffect {
    data object NavigateBack : WriteReviewSideEffect

    /** 리뷰 등록 완료 → 작가 리뷰 상세로 이동 */
    data class NavigateToReviewDetail(val reviewId: Int) : WriteReviewSideEffect
}
