package com.hm.picplz.ui.screen.login

sealed class LoginSideEffect {
    object NavigateToKaKao : LoginSideEffect()
}