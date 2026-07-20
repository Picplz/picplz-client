package com.hm.picplz.ui.screen.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.feature.main.R as FeatureMainR

private object SearchNavigateButtonDefaults {
    val HorizontalPadding = 16.dp
    val VerticalPadding = 10.dp
    val CornerRadius = 50.dp
    val StrokeWidth = 1.dp
    val Gap = 10.dp
    val IconSize = 20.dp
    val TextLineHeight = 20.sp
}

@Composable
fun SearchNavigateButton(
    modifier: Modifier = Modifier,
    placeholder: String,
    onClick: () -> Unit = { },
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(SearchNavigateButtonDefaults.CornerRadius))
                .background(MainThemeColor.White)
                .border(
                    width = SearchNavigateButtonDefaults.StrokeWidth,
                    color = MainThemeColor.Gray6,
                    shape = RoundedCornerShape(SearchNavigateButtonDefaults.CornerRadius),
                )
                .clickable { onClick() }
                .padding(
                    horizontal = SearchNavigateButtonDefaults.HorizontalPadding,
                    vertical = SearchNavigateButtonDefaults.VerticalPadding,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SearchNavigateButtonDefaults.Gap),
        ) {
            Text(
                text = placeholder,
                style = MainThemeFont.Body.copy(lineHeight = SearchNavigateButtonDefaults.TextLineHeight),
                color = MainThemeColor.Gray3,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(id = FeatureMainR.drawable.main_search_icon),
                contentDescription = stringResource(FeatureMainR.string.main_search_icon_content_description),
                tint = MainThemeColor.Gray6,
                modifier = Modifier.size(SearchNavigateButtonDefaults.IconSize),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchNavigateButtonPreview() {
    PicplzTheme {
        SearchNavigateButton(
            placeholder = "촬영을 하고 싶은 장소 또는 동을 검색해보세요",
            onClick = {},
        )
    }
}
