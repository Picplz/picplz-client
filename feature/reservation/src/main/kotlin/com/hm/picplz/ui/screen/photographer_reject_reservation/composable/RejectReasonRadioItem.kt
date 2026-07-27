package com.hm.picplz.ui.screen.photographer_reject_reservation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

/**
 * 단일 선택 라디오 + 라벨. 거절 사유 선택에 사용합니다.
 * (취소 플로우의 다중 선택 [com.hm.picplz.ui.screen.cancel_reservation.composable.CheckboxWithLabel]와 구분)
 */
@Composable
fun RejectReasonRadioItem(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onSelect() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RejectReasonRadio(selected = selected)

        Text(
            text = text,
            style = if (selected) MainThemeFont.BodyBold else MainThemeFont.Body,
            color = if (selected) MainThemeColor.Black else MainThemeColor.Gray4,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
        )
    }
}

@Composable
private fun RejectReasonRadio(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(20.dp)
                .border(
                    border =
                        BorderStroke(
                            width = 1.5.dp,
                            color = if (selected) MainThemeColor.Black else MainThemeColor.Gray3,
                        ),
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier =
                    Modifier
                        .size(10.dp)
                        .background(color = MainThemeColor.Black, shape = CircleShape),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RejectReasonRadioItemUnselectedPreview() {
    PicplzTheme {
        RejectReasonRadioItem(
            text = "해당 지역은 촬영이 어려워요",
            selected = false,
            onSelect = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RejectReasonRadioItemSelectedPreview() {
    PicplzTheme {
        RejectReasonRadioItem(
            text = "직접 입력",
            selected = true,
            onSelect = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
