package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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
            is SetPhotographyExperience -> {
                val newPhotographyExperienceState = _state.value.copy(
                    hasPhotographyExperience = if (_state.value.hasPhotographyExperience == intent.hasExperience) {
                        null
                    } else {
                        intent.hasExperience
                    }
                )
                _state.value = newPhotographyExperienceState
            }
        }
    }
}