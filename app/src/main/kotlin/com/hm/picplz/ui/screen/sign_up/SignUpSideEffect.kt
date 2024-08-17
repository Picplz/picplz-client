package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.User

sealed class SignUpSideEffect {
    data class NavigateToSetting(
        val destination: Destination,
        val user: User
    ) : SignUpSideEffect()
}

enum class Destination(val route: String) {
    SignUpClient("sign-up-client"),
    SignUpPhotographer("sign-up-photographer")
}