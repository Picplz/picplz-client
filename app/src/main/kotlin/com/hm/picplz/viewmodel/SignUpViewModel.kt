package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.DestinationByUserType.*
import com.hm.picplz.ui.screen.sign_up.SignUpIntent
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.screen.sign_up.SignUpSideEffect
import com.hm.picplz.ui.screen.sign_up.SignUpState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.hm.picplz.data.model.User
import com.hm.picplz.data.model.UserType.*

class SignUpViewModel : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.idle())
    val state: StateFlow<SignUpState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> get() = _sideEffect

    /**
     * 임시 데이터
     * Todo: 카카오 로그인에서 반환받은 유저 정보 호출
     */
    private var userData: User = emptyUserData

    fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.NavigateToPrev)
                }
            }
            is ClickUserTypeButton -> {
                val newUserType = if (_state.value.selectedUserType == intent.userType) {
                    null
                } else {
                    intent.userType
                }

                _state.value = _state.value.copy(selectedUserType = newUserType)
            }
            is NavigateToSelected -> {
                viewModelScope.launch {
                    _state.value.selectedUserType?.let { selectedUserType ->
                        val destination = when (selectedUserType) {
                            User -> SignUpClient
                            Photographer -> SignUpPhotographer
                        }
                        _sideEffect.emit(SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected(destination, userData))
                    }
                }
            }
            is ResetSelectedUserType -> {
                _state.value = _state.value.copy(selectedUserType = null)
            }
            is SetNickname -> {
                val newNicknameState = _state.value.copy(nickname = intent.newNickname)
                _state.value = newNicknameState
            }
            is SetProfileImageUri -> {
                val newProfileImageUriState = _state.value.copy(profileImageUri = intent.newProfileImageUri)
                _state.value = newProfileImageUriState
            }
            is Navigate -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.Navigate(intent.destination))
                }
            }
        }
    }
}

val emptyUserData = User(
    id = 0,
    name = "Unknown",
    email = "unknown@example.com"
)