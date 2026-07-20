package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CommonLocationPermissionRationale(
    titlePrefix: String,
    highlightedTitle: String,
    titleSuffix: String,
    description: String,
    buttonText: String,
    iconContentDescription: String,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MainThemeColor.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 162.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_location_permission),
                contentDescription = iconContentDescription,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = titlePrefix,
                style = MainThemeFont.Title,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = MainThemeColor.Green120)) {
                            append(highlightedTitle)
                        }
                        withStyle(SpanStyle(color = MainThemeColor.Black)) {
                            append(titleSuffix)
                        }
                    },
                style = MainThemeFont.Title,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = description,
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }

        CommonBottomButton(
            text = buttonText,
            onClick = onNextClick,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 20.dp, end = 20.dp, bottom = 47.dp),
            enabled = true,
        )
    }
}

@Composable
fun CommonLocationPermissionDeniedContent(
    title: String,
    subtitle: String,
    guidePrefix: String,
    guideSuffix: String,
    imageContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )
        Text(
            text = subtitle,
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.no_place),
            contentDescription = imageContentDescription,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = guidePrefix,
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
        Text(
            text = guideSuffix,
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonLocationPermissionRationalePreview() {
    PicplzTheme {
        CommonLocationPermissionRationale(
            titlePrefix = "주변 작가를 찾으려면",
            highlightedTitle = "위치 권한",
            titleSuffix = "이 필요해요",
            description = "현재 위치를 기준으로 가까운 작가를 보여드려요",
            buttonText = "다음",
            iconContentDescription = "위치 권한",
            onNextClick = {},
        )
    }
}
