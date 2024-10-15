package com.hm.picplz.viewmodel.common

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.*
import com.hm.picplz.ui.screen.common.common_chip.CommonChipState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CommonChipViewModel : ViewModel() {

    private val _state = MutableStateFlow(CommonChipState.idle())
    val state: StateFlow<CommonChipState> = _state

    fun handleIntent(intent: CommonChipIntent) {
        when (intent) {
            is SetInitialValue -> {
                _state.update { it.copy(value = intent.value) }
            }
            is StartEditing -> {
                _state.update { it.copy(isEditing = true) }
            }
            is UpdateValue -> {
                _state.update { it.copy(value = intent.value) }
            }
            is FinishEditing -> {
                _state.update { it.copy(isEditing = false) }
            }
            is SetChipMode -> {
                _state.update { it.copy(chipMode = intent.newChipMode)}
            }
        }
    }
}

