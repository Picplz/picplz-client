package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.Destination.*
import com.hm.picplz.ui.screen.sign_up.SignUpIntent
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.screen.sign_up.SignUpSideEffect
import com.hm.picplz.ui.screen.sign_up.SignUpViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.hm.picplz.data.model.User
import com.hm.picplz.data.model.UserType.*

class SignUpViewModel : ViewModel() {

    private val _state = MutableStateFlow<SignUpViewState>(SignUpViewState.idle())
    val state: StateFlow<SignUpViewState> get() = _state

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
                        _sideEffect.emit(SignUpSideEffect.NavigateToSelected(destination, userData))
                    }
                }
            }
            is ResetSelectedUserType -> {
                _state.value = SignUpViewState.idle()
            }
        }
    }
}

val emptyUserData = User(
    id = 0,
    name = "Unknown",
    email = "unknown@example.com"
)