package com.hm.picplz.ui.screen.common.common_chip

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.data.model.ChipMode

data class CommonChipState(
    val value: String = "",
    val chipMode: ChipMode = ChipMode.DEFAULT,
    val calculatedWidth: Dp = 0.dp,
    val textFieldWidth: Dp = 0.dp,
) {
    companion object {
        fun idle(): CommonChipState {
            return CommonChipState(
                value = "",
                chipMode = ChipMode.DEFAULT,
                calculatedWidth = 0.dp,
                textFieldWidth = 0.dp,
            )
        }
    }
}