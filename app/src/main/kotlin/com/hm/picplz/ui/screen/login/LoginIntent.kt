package com.hm.picplz.ui.screen.login

sealed class LoginIntent {
    object NavigateToKaKao : LoginIntent()
}