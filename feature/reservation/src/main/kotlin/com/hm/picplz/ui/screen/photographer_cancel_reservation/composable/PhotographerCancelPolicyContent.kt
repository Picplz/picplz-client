package com.hm.picplz.ui.screen.photographer_cancel_reservation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PhotographerCancelPolicyContent(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.photographer_cancel_policy_title),
            style = MainThemeFont.Title,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
        )

        Text(
            text = stringResource(R.string.photographer_cancel_policy_penalty_criteria_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        CancelPenaltyTable()

        Text(
            text = stringResource(R.string.photographer_cancel_policy_guide_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            modifier = Modifier.padding(top = 28.dp, bottom = 12.dp),
        )

        GuideLabel(text = stringResource(R.string.photographer_cancel_policy_guide_activity_label))
        GuideBullet(text = stringResource(R.string.photographer_cancel_policy_guide_activity_1))
        GuideBullet(text = stringResource(R.string.photographer_cancel_policy_guide_activity_2))

        GuideLabel(
            text = stringResource(R.string.photographer_cancel_policy_guide_system_label),
            modifier = Modifier.padding(top = 8.dp),
        )
        GuideBullet(text = stringResource(R.string.photographer_cancel_policy_guide_system_1))
    }
}

@Composable
private fun GuideLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MainThemeFont.BodyBold,
        color = MainThemeColor.Black,
        modifier = modifier.padding(bottom = 4.dp),
    )
}

@Composable
private fun GuideBullet(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 4.dp),
    ) {
        Text(
            text = "• ",
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
        Text(
            text = text,
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun PhotographerCancelPolicyContentPreview() {
    PicplzTheme {
        PhotographerCancelPolicyContent()
    }
}
