package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.Destination.*
import com.hm.picplz.ui.screen.sign_up.SignUpIntent
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.screen.sign_up.SignUpSideEffect
import com.hm.picplz.ui.screen.sign_up.UserType
import com.hm.picplz.ui.screen.sign_up.UserType.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import com.hm.picplz.data.model.User

class SignUpViewModel : ViewModel() {
    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> get() = _sideEffect

    /**
     * 임시 데이터
     * Todo: 카카오 로그인에서 반환받은 유저 정보 호출
     */
    private var userData: User = emptyUserData

    fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SelectUserType -> {
                val destination = when (intent.userType) {
                    User -> SignUpClient
                    Photographer -> SignUpPhotographer
                }
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.NavigateToSetting(destination, userData))
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