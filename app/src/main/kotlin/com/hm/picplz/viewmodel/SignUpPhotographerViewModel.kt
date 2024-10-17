package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SignUpPhotographerViewModel : ViewModel() {
    private val _state = MutableStateFlow<SignUpPhotographerState>(SignUpPhotographerState.idle())
    val state: StateFlow<SignUpPhotographerState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpPhotographerSideEffect>()
    val sideEffect: SharedFlow<SignUpPhotographerSideEffect> get() = _sideEffect

    fun handleIntent(intent: SignUpPhotographerIntent) {
        when (intent) {
            is NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpPhotographerSideEffect.NavigateToPrev)
                }
            }
            is SetUserInfo -> {}
            is SetHasPhotographyExperience -> {
                val newPhotographyExperienceState = _state.value.copy(
                    hasPhotographyExperience = if (_state.value.hasPhotographyExperience == intent.hasExperience) {
                        null
                    } else {
                        intent.hasExperience
                    }
                )
                _state.value = newPhotographyExperienceState
            }
            is Navigate -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpPhotographerSideEffect.Navigate(intent.destination))
                }
            }
            is SetPhotographyExperience -> {
                _state.update { it.copy(photographyExperienceId = intent.photographyExperienceId)}
            }
            is SetEditingChipId -> {
                _state.update { it.copy(editingChipId = intent.chipId)}
            }
            is AddVibeChip -> {
                val maxId = _state.value.vibeChipList
                    .maxByOrNull { it.id.toIntOrNull() ?: 0 }?.id?.toIntOrNull() ?: 0
                val newId = (maxId + 1).toString()

                val newChip = ChipItem(id = newId, label = intent.label, isEditable = true)
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList + newChip
                    currentState.copy(experienceChipList = updatedChipList)
                }
            }
            is DeleteVibeChip -> {
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList.filter { it.id != intent.chipId }
                    currentState.copy(experienceChipList = updatedChipList)
                }
            }
            is UpdateVibeChip -> {
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList.map { chip ->
                        if (chip.id == intent.chipId) {
                            chip.copy(label = intent.label)
                        } else {
                            chip
                        }
                    }
                    currentState.copy(experienceChipList = updatedChipList)
                }
            }
        }
    }
}