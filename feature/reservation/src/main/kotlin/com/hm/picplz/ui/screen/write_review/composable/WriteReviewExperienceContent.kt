package com.hm.picplz.ui.screen.write_review.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.cancel_reservation.composable.DirectInputTextField
import com.hm.picplz.ui.screen.write_review.REVIEW_CONTENT_MAX_LENGTH
import com.hm.picplz.ui.screen.write_review.REVIEW_CONTENT_MIN_LENGTH
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun WriteReviewExperienceContent(
    contentText: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(R.string.write_review_content_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
        )

        Text(
            text = stringResource(R.string.write_review_content_subtitle),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
        )

        DirectInputTextField(
            value = contentText,
            onValueChange = onContentChange,
            maxLength = REVIEW_CONTENT_MAX_LENGTH,
            placeholder = stringResource(R.string.write_review_content_placeholder, REVIEW_CONTENT_MIN_LENGTH),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewExperienceContentPreviewEmpty() {
    PicplzTheme {
        WriteReviewExperienceContent(
            contentText = "",
            onContentChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewExperienceContentPreviewFilled() {
    PicplzTheme {
        WriteReviewExperienceContent(
            contentText = "재밌었어요 사진도 잘찍으심",
            onContentChange = {},
        )
    }
}
