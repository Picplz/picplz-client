package com.hm.picplz.ui.screen.common.common_chip

import androidx.compose.ui.unit.Dp
import com.hm.picplz.data.model.ChipMode

sealed class CommonChipIntent {
    data class SetValue(val value: String) : CommonChipIntent()
    data class SetChipMode(val newChipMode: ChipMode) : CommonChipIntent()
    data class SetTextFieldWidth(val newWidth: Dp) : CommonChipIntent()
    data class SetCalculatedWidth(val newWidth: Dp) : CommonChipIntent()
}
