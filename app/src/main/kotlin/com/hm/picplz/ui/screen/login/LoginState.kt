package com.hm.picplz.ui.screen.login

sealed class LoginState {
    object Idle : LoginState()
    data class Navigate(val url: String) : LoginState()
}