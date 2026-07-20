package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonLocationPermissionDeniedContent
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
internal fun MainLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MainThemeColor.Black)
    }
}

@Composable
internal fun MainPermissionDeniedContent(onRetry: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 88.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CommonLocationPermissionDeniedContent(
            title = stringResource(R.string.main_location_denied_title),
            subtitle = stringResource(R.string.main_location_denied_subtitle),
            guidePrefix = stringResource(R.string.main_location_denied_guide_prefix),
            guideSuffix = stringResource(R.string.main_location_denied_guide_suffix),
            imageContentDescription = stringResource(R.string.main_location_denied_image_desc),
        )
        Spacer(modifier = Modifier.height(28.dp))
        CommonBottomButton(
            text = stringResource(R.string.main_retry_button),
            onClick = onRetry,
        )
    }
}

@Composable
internal fun MainEmptyContent(onRetry: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.main_empty_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.main_empty_subtitle),
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(28.dp))
        CommonBottomButton(
            text = stringResource(R.string.main_retry_button),
            onClick = onRetry,
        )
    }
}
