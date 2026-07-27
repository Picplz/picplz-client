package com.hm.picplz.ui.screen.write_review

import androidx.annotation.StringRes
import com.hm.picplz.feature.reservation.R

/** "어떤점이 아쉬웠나요?" 입력란 최대 글자 수 */
const val NEGATIVE_FEEDBACK_MAX_LENGTH = 600

/** 이 점수 이하를 부정 평가로 보고 아쉬운 점 입력란을 노출합니다. */
private const val NEGATIVE_FEEDBACK_MAX_SCORE = 2

/**
 * 리뷰 별점(1~5)과 점수별 안내 문구.
 */
enum class ReviewRating(
    val score: Int,
    @get:StringRes val mainMessageRes: Int,
    @get:StringRes val subMessageRes: Int,
) {
    VERY_BAD(1, R.string.write_review_rating_main_1, R.string.write_review_rating_sub_1),
    BAD(2, R.string.write_review_rating_main_2, R.string.write_review_rating_sub_2),
    OKAY(3, R.string.write_review_rating_main_3, R.string.write_review_rating_sub_3),
    GOOD(4, R.string.write_review_rating_main_4, R.string.write_review_rating_sub_4),
    EXCELLENT(5, R.string.write_review_rating_main_5, R.string.write_review_rating_sub_5),
    ;

    /** 부정 평가(1~2점)일 때만 아쉬운 점 입력란을 노출합니다. */
    val needsNegativeFeedback: Boolean
        get() = score <= NEGATIVE_FEEDBACK_MAX_SCORE

    companion object {
        fun fromScore(score: Int): ReviewRating? = entries.firstOrNull { it.score == score }
    }
}
