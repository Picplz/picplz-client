package com.hm.picplz.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.NavigateToKaKao -> navigateToKaKao()
        }
    }

    private fun navigateToKaKao() {
        viewModelScope.launch {
            // 소셜 로그인 관련 로직 추가
        }
    }
}