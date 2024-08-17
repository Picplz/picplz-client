package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.Destination.*
import com.hm.picplz.ui.screen.sign_up.SignUpIntent
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.screen.sign_up.SignUpSideEffect
import com.hm.picplz.ui.screen.sign_up.UserType.*
import kotlinx.coroutines.flow.MutableSharedFlow
]import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> get() = _sideEffect

    fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SelectUserType -> {
                val destination = when (intent.userType) {
                    USER -> SignUpClient
                    PHOTOGRAPHER -> SignUpPhotographer
                }
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.NavigateToSetting(destination))
                }
            }
        }
    }
}