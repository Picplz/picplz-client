package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

private const val CANCEL_REASON_INPUT_MAX_LENGTH = 100

@Composable
fun DirectInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = CANCEL_REASON_INPUT_MAX_LENGTH,
    placeholder: String = stringResource(R.string.cancel_reason_input_placeholder, maxLength),
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = { newText ->
                if (newText.length <= maxLength) {
                    onValueChange(newText)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MainThemeFont.Body,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MainThemeColor.Gray2,
                                shape = RoundedCornerShape(5.dp),
                            )
                            .background(
                                color = MainThemeColor.Gray1,
                                shape = RoundedCornerShape(5.dp),
                            )
                            .height(136.dp)
                            .padding(12.dp),
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MainThemeFont.Body,
                            color = MainThemeColor.Gray3,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DirectInputTextFieldPreviewEmpty() {
    PicplzTheme {
        DirectInputTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DirectInputTextFieldPreviewWithText() {
    PicplzTheme {
        DirectInputTextField(
            value = "촬영 날짜를 변경하고 싶은데, 사진작가님이 다른 일정이 있다고 하셔서 취소하게 되었습니다.",
            onValueChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DirectInputTextFieldPreviewNearMaxLength() {
    PicplzTheme {
        val longText = "a".repeat(90)
        DirectInputTextField(
            value = longText,
            onValueChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
