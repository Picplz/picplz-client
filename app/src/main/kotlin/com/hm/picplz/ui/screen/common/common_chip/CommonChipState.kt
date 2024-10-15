package com.hm.picplz.ui.screen.common.common_chip

import com.hm.picplz.data.model.ChipMode

data class CommonChipState(
    val value: String = "",
    val chipMode: ChipMode = ChipMode.DEFAULT
) {
    companion object {
        fun idle(): CommonChipState {
            return CommonChipState(
                value = "",
                chipMode = ChipMode.DEFAULT
            )
        }
    }
}