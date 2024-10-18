package com.hm.picplz.ui.screen.common.common_chip

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.data.model.ChipMode

data class CommonChipState(
    val value: TextFieldValue = TextFieldValue(""),
    val chipMode: ChipMode = ChipMode.DEFAULT,
    val calculatedWidth: Dp = 0.dp,
    val textFieldWidth: Dp = 0.dp,
    val isEditing: Boolean = false,
) {
    companion object {
        fun idle(): CommonChipState {
            return CommonChipState(
                value = TextFieldValue(""),
                chipMode = ChipMode.DEFAULT,
                calculatedWidth = 0.dp,
                textFieldWidth = 0.dp,
                isEditing = false,
            )
        }
    }
}