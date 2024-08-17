package com.hm.picplz.ui.screen.sign_up


sealed class SignUpSideEffect {
    data class NavigateToSetting(val destination: Destination) : SignUpSideEffect()
}

enum class Destination(val route: String) {
    SignUpClient("sign-up-client"),
    SignUpPhotographer("sign-up-photographer")
}