package com.hm.picplz.ui.screen.write_review.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.cancel_reservation.composable.DirectInputTextField
import com.hm.picplz.ui.screen.common.StarRatingSelector
import com.hm.picplz.ui.screen.write_review.NEGATIVE_FEEDBACK_MAX_LENGTH
import com.hm.picplz.ui.screen.write_review.ReviewRating
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun WriteReviewRatingContent(
    customerNickname: String,
    photographerName: String,
    selectedRating: ReviewRating?,
    negativeFeedbackText: String,
    onRatingSelect: (ReviewRating) -> Unit,
    onNegativeFeedbackChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(R.string.write_review_rating_title, customerNickname, photographerName),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
        )

        Text(
            text = stringResource(R.string.write_review_rating_subtitle),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray3,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
        )

        StarRatingSelector(
            rating = selectedRating?.score ?: 0,
            onRatingChange = { score -> ReviewRating.fromScore(score)?.let(onRatingSelect) },
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 56.dp),
        )

        ReviewRatingMessage(
            rating = selectedRating,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 30.dp),
        )

        if (selectedRating?.needsNegativeFeedback == true) {
            Text(
                text = stringResource(R.string.write_review_negative_feedback_label),
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp),
            )

            DirectInputTextField(
                value = negativeFeedbackText,
                onValueChange = onNegativeFeedbackChange,
                maxLength = NEGATIVE_FEEDBACK_MAX_LENGTH,
                placeholder = stringResource(R.string.write_review_negative_feedback_placeholder),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
            )
        }
    }
}

/** 별점별 안내 문구. 미선택 시에는 별점을 매겨달라는 안내를 보여줍니다. */
@Composable
private fun ReviewRatingMessage(
    rating: ReviewRating?,
    modifier: Modifier = Modifier,
) {
    val mainMessageRes = rating?.mainMessageRes ?: R.string.write_review_rating_empty_main
    val subMessageRes = rating?.subMessageRes ?: R.string.write_review_rating_empty_sub

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(mainMessageRes),
            style = MainThemeFont.BodySmallButton2,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(subMessageRes),
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray5,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewRatingContentPreviewEmpty() {
    PicplzTheme {
        WriteReviewRatingContent(
            customerNickname = "세연",
            photographerName = "유가영",
            selectedRating = null,
            negativeFeedbackText = "",
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewRatingContentPreviewNegative() {
    PicplzTheme {
        WriteReviewRatingContent(
            customerNickname = "세연",
            photographerName = "유가영",
            selectedRating = ReviewRating.BAD,
            negativeFeedbackText = "",
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewRatingContentPreviewExcellent() {
    PicplzTheme {
        WriteReviewRatingContent(
            customerNickname = "세연",
            photographerName = "유가영",
            selectedRating = ReviewRating.EXCELLENT,
            negativeFeedbackText = "",
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
        )
    }
}
