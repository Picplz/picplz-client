package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

/** 선택 가능한 최대 별점 */
const val MAX_STAR_RATING = 5

private val defaultStarSize = 42.dp
private val defaultStarSpacing = 4.dp

/**
 * 탭해서 1점 단위로 별점을 고르는 컴포넌트.
 *
 * 조회 전용 별점 표시는 [com.hm.picplz.ui.util.ReviewUtil.calculateStarRating]을 사용하세요.
 *
 * @param rating 현재 선택된 별점(1..[MAX_STAR_RATING]). 미선택은 0.
 */
@Composable
fun StarRatingSelector(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Dp = defaultStarSize,
    starSpacing: Dp = defaultStarSpacing,
    selectedColor: Color = MainThemeColor.Green120,
    unselectedColor: Color = MainThemeColor.Gray2,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(starSpacing),
    ) {
        for (score in 1..MAX_STAR_RATING) {
            val interactionSource = remember { MutableInteractionSource() }

            Icon(
                painter = painterResource(R.drawable.star_full),
                contentDescription = stringResource(R.string.star_rating_score_format, score),
                tint = if (score <= rating) selectedColor else unselectedColor,
                modifier =
                    Modifier
                        .size(starSize)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) { onRatingChange(score) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StarRatingSelectorPreviewEmpty() {
    PicplzTheme {
        StarRatingSelector(rating = 0, onRatingChange = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun StarRatingSelectorPreviewPartial() {
    PicplzTheme {
        StarRatingSelector(rating = 2, onRatingChange = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun StarRatingSelectorPreviewFull() {
    PicplzTheme {
        StarRatingSelector(rating = MAX_STAR_RATING, onRatingChange = {})
    }
}
