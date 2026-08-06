package com.hm.picplz.ui.screen.write_review

sealed interface WriteReviewSideEffect {
    data object NavigateBack : WriteReviewSideEffect
}
