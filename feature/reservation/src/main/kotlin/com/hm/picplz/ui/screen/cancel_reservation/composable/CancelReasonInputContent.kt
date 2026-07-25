package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.cancel_reservation.CancelReason
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CancelReasonInputContent(
    selectedReasons: Set<CancelReason>,
    directInputText: String,
    onReasonToggle: (CancelReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.cancel_reason_input_title),
            style = MainThemeFont.Title,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 22.dp),
        ) {
            item {
                Text(
                    text = stringResource(R.string.cancel_reason_input_subtitle),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Green120,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                )
            }

            items(CancelReason.entries) { reason ->
                CheckboxWithLabel(
                    text = stringResource(reason.stringRes),
                    isSelected = selectedReasons.contains(reason),
                    onToggle = { onReasonToggle(reason) },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                )
            }

            if (selectedReasons.contains(CancelReason.DIRECT_INPUT)) {
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = expandVertically(),
                        exit = shrinkVertically(),
                    ) {
                        DirectInputTextField(
                            value = directInputText,
                            onValueChange = onDirectInputChange,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelReasonInputContentPreviewEmpty() {
    PicplzTheme {
        CancelReasonInputContent(
            selectedReasons = emptySet(),
            directInputText = "",
            onReasonToggle = {},
            onDirectInputChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelReasonInputContentPreviewWithSelection() {
    PicplzTheme {
        CancelReasonInputContent(
            selectedReasons =
                setOf(
                    CancelReason.SCHEDULE,
                    CancelReason.MIND,
                ),
            directInputText = "",
            onReasonToggle = {},
            onDirectInputChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelReasonInputContentPreviewWithDirectInput() {
    PicplzTheme {
        CancelReasonInputContent(
            selectedReasons = setOf(CancelReason.DIRECT_INPUT),
            directInputText = "촬영 위치가 너무 멀어서 취소합니다.",
            onReasonToggle = {},
            onDirectInputChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelReasonInputContentPreviewFull() {
    PicplzTheme {
        CancelReasonInputContent(
            selectedReasons =
                setOf(
                    CancelReason.SCHEDULE,
                    CancelReason.PRODUCT,
                    CancelReason.DIRECT_INPUT,
                ),
            directInputText = "개인적인 사유로 취소하게 되었습니다.",
            onReasonToggle = {},
            onDirectInputChange = {},
        )
    }
}
