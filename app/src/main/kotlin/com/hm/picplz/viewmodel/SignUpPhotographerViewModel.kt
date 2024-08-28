package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerState
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
            is ChangeStep -> {
                val newStepState = _state.value.copy(currentStep = intent.stepNum)
                _state.value = newStepState
            }
            is SetNickname -> {
                val newNicknameState = _state.value.copy(nickname = intent.newNickname)
                _state.value = newNicknameState
            }
            is ClickSubmitButton -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpPhotographerSideEffect.SubmitProfileInfo(
                        nickname = _state.value.nickname,
                        profileImageUri = _state.value.profileImageUri,
                    ))
                }
            }
            is SetUserInfo -> {}
            is SetProfileImageUri -> {
                val newProfileImageUriState = _state.value.copy(profileImageUri = intent.newProfileImageUri)
                _state.value = newProfileImageUriState
            }
        }
    }
}