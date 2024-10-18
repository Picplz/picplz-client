package com.hm.picplz.viewmodel

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.data.model.PhotographyExperience
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
            is SetUserInfo -> {
                _state.update { it.copy(userInfo = intent.userInfo)}
            }
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
                _state.update { it.copy(selectedPhotographyExperienceId = intent.photographyExperienceId)}
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
                    currentState.copy(vibeChipList = updatedChipList)
                }
            }
            is DeleteVibeChip -> {
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList.filter { it.id != intent.chipId }
                    currentState.copy(vibeChipList = updatedChipList)
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
                    currentState.copy(vibeChipList = updatedChipList)
                }
            }
            is UpdateSelectedVibeChipList -> {
                _state.update { currentState ->
                    val updateSelectedChipList = if (currentState.selectedVibeChipList.any {it.id == intent.chipId}) {
                        currentState.selectedVibeChipList.filter { it.id != intent.chipId }
                    } else {
                        currentState.selectedVibeChipList + ChipItem(id = intent.chipId, label = intent.label)
                    }
                    currentState.copy(selectedVibeChipList = updateSelectedChipList)
                }
            }
            is SetUserPhotographyExperience -> {
                val experience = when (_state.value.selectedPhotographyExperienceId) {
                    "1" -> PhotographyExperience.PHOTO_MAJOR
                    "2" -> PhotographyExperience.INCOME_GENERATION
                    "3" -> PhotographyExperience.SNS_OPERATION
                    else -> null
                }

                experience?.let { newExperience ->
                    val updatedUser = _state.value.userInfo.copy(
                        photographyExperience = newExperience
                    )
                    _state.update { it.copy( userInfo = updatedUser ) }
                }
            }
            is NavigateWithSubmit -> {
                viewModelScope.launch {
                    val userBundle  = if (_state.value.vibeChipList.isNotEmpty()) {
                        bundleOf(
                            "userInfo" to _state.value.userInfo
                        )
                    } else bundleOf()
                    _sideEffect.emit(SignUpPhotographerSideEffect.NavigateWithSubmit(intent.destination, userBundle))
                }
            }
            is SetUserPhotographyVibe -> {
                val updatedUser = _state.value.userInfo.copy(
                    photographyVibes = _state.value.selectedVibeChipList.map { chip -> chip.label }
                )
                _state.update { it.copy(userInfo = updatedUser) }
            }
        }
    }
}