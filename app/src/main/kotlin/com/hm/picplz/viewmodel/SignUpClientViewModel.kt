package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientIntent
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientIntent.*
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientSideEffect
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerSideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpClientViewModel : ViewModel() {
    private val _state = MutableStateFlow<SignUpClientState>(SignUpClientState.idle())
    val state: StateFlow<SignUpClientState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpClientSideEffect>()
    val sideEffect: SharedFlow<SignUpClientSideEffect> get() = _sideEffect

    fun handleIntent(intent: SignUpClientIntent) {
        when (intent) {
            is SetUserInfo -> {}
            is SetNickName -> {
                val newNicknameState = _state.value.copy(nickname = intent.newNickname)
                _state.value = newNicknameState
            }
            is SetProfileImageUri -> {
                val newProfileImageUrlState = _state.value.copy(profileImageUri = intent.newProfileImageUri)
                _state.value = newProfileImageUrlState
            }
            is NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpClientSideEffect.NavigateToPrev)
                }
            }
            is ChangeStep -> {
                val newStepState = _state.value.copy(currentStep = intent.stepNum)
                _state.value = newStepState
            }
            is ClickSubmitButton -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpClientSideEffect.SubmitProfileInfo(
                        nickname = _state.value.nickname,
                        profileImageUrl = _state.value.profileImageUri,
                    ))
                }
            }
        }
    }
}