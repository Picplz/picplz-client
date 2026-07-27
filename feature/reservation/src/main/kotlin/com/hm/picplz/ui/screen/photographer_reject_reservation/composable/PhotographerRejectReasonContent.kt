package com.hm.picplz.ui.screen.photographer_reject_reservation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.hm.picplz.ui.screen.cancel_reservation.composable.DirectInputTextField
import com.hm.picplz.ui.screen.photographer_reject_reservation.PhotographerRejectReason
import com.hm.picplz.ui.screen.photographer_reject_reservation.REJECT_REASON_MAX_LENGTH
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

/** 라벨 텍스트 시작 위치 정렬용 들여쓰기 (좌측 여백 16 + 라디오 20 + 간격 10) */
private val labelIndent = 46.dp

@Composable
fun PhotographerRejectReasonContent(
    selectedReason: PhotographerRejectReason?,
    directInputText: String,
    onReasonSelect: (PhotographerRejectReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.reject_reason_input_title),
            style = MainThemeFont.Title,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(PhotographerRejectReason.entries) { reason ->
                Column {
                    RejectReasonRadioItem(
                        text = stringResource(reason.stringRes),
                        selected = selectedReason == reason,
                        onSelect = { onReasonSelect(reason) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    if (reason == PhotographerRejectReason.TIME) {
                        Text(
                            text = stringResource(R.string.reject_reason_option_time_note),
                            style = MainThemeFont.Caption,
                            color = MainThemeColor.Red,
                            modifier = Modifier.padding(start = labelIndent, end = 16.dp, top = 8.dp),
                        )
                    }
                }
            }

            if (selectedReason == PhotographerRejectReason.DIRECT_INPUT) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = stringResource(R.string.reject_reason_direct_input_label),
                            style = MainThemeFont.TitleSmall,
                            color = MainThemeColor.Black,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        DirectInputTextField(
                            value = directInputText,
                            onValueChange = onDirectInputChange,
                            maxLength = REJECT_REASON_MAX_LENGTH,
                            placeholder =
                                stringResource(
                                    R.string.reject_reason_input_placeholder,
                                    REJECT_REASON_MAX_LENGTH,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotographerRejectReasonContentPreviewEmpty() {
    PicplzTheme {
        PhotographerRejectReasonContent(
            selectedReason = null,
            directInputText = "",
            onReasonSelect = {},
            onDirectInputChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotographerRejectReasonContentPreviewDirectInput() {
    PicplzTheme {
        PhotographerRejectReasonContent(
            selectedReason = PhotographerRejectReason.DIRECT_INPUT,
            directInputText = "다른 지역 일정과 겹쳤어요",
            onReasonSelect = {},
            onDirectInputChange = {},
        )
    }
}
