package com.hm.picplz.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> get() = _state

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect: SharedFlow<LoginSideEffect> get() = _sideEffect

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.NavigateToKaKao -> {
                viewModelScope.launch {
                    _sideEffect.emit(LoginSideEffect.NavigateToKaKao)
                }
            }
        }
    }
}