package com.hm.picplz.ui.screen.common.common_chip

import com.hm.picplz.data.model.ChipMode

sealed class CommonChipIntent {
    data class SetInitialValue(val value: String) : CommonChipIntent()
    data object StartEditing : CommonChipIntent()
    data class UpdateValue(val value: String) : CommonChipIntent()
    data object FinishEditing : CommonChipIntent()
    data class SetChipMode(val newChipMode: ChipMode) : CommonChipIntent()
}
