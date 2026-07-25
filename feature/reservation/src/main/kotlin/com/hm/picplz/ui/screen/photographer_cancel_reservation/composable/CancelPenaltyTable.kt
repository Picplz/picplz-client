package com.hm.picplz.ui.screen.photographer_cancel_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

/**
 * 작가 취소 페널티 부여 기준 표. "고객과 사전 합의됨" 여부에 따른 경고 부여 기준을 안내합니다.
 */
@Composable
fun CancelPenaltyTable(modifier: Modifier = Modifier) {
    val rows =
        listOf(
            stringResource(R.string.photographer_cancel_policy_table_not_agreed) to
                stringResource(R.string.photographer_cancel_policy_table_not_agreed_penalty),
            stringResource(R.string.photographer_cancel_policy_table_agreed) to
                stringResource(R.string.photographer_cancel_policy_table_agreed_penalty),
        )

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MainThemeColor.Gray3,
                        shape = RoundedCornerShape(8.dp),
                    ),
        ) {
            // 헤더
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .background(MainThemeColor.Black),
            ) {
                HeaderCell(
                    text = stringResource(R.string.photographer_cancel_policy_table_header_situation),
                    weight = 0.6f,
                )
                HeaderCell(
                    text = stringResource(R.string.photographer_cancel_policy_table_header_penalty),
                    weight = 0.4f,
                )
            }

            // 각 행
            rows.forEach { (situation, penalty) ->
                HorizontalDivider(color = MainThemeColor.Gray3, thickness = 1.dp)

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                ) {
                    BodyCell(text = situation, weight = 0.6f)
                    VerticalDivider(color = MainThemeColor.Gray3, thickness = 1.dp)
                    BodyCell(text = penalty, weight = 0.4f)
                }
            }
        }

        Text(
            text = stringResource(R.string.photographer_cancel_policy_table_note),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Red,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun RowScope.HeaderCell(
    text: String,
    weight: Float,
    textColor: Color = MainThemeColor.White,
) {
    Text(
        text = text,
        modifier =
            Modifier
                .weight(weight)
                .padding(vertical = 12.dp, horizontal = 8.dp),
        color = textColor,
        style = MainThemeFont.BodyBold,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun RowScope.BodyCell(
    text: String,
    weight: Float,
    textColor: Color = MainThemeColor.Gray4,
) {
    Text(
        text = text,
        modifier =
            Modifier
                .weight(weight)
                .padding(vertical = 12.dp, horizontal = 8.dp),
        color = textColor,
        style = MainThemeFont.Caption,
        textAlign = TextAlign.Center,
    )
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CancelPenaltyTablePreview() {
    PicplzTheme {
        CancelPenaltyTable(modifier = Modifier.padding(16.dp))
    }
}
