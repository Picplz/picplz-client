package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.User

sealed class SignUpSideEffect {
    sealed class SelectUserTypeScreenSideEffect {
        data class NavigateToSelected (
            val destination: DestinationByUserType,
            val user: User
        ) : SignUpSideEffect()
    }
    data object NavigateToPrev : SignUpSideEffect()
    data class Navigate(val destination: String) : SignUpSideEffect()
    data object ShowFileUploadDialog : SignUpSideEffect()
}

enum class DestinationByUserType(val route: String) {
    SignUpClient("sign-up-client"),
    SignUpPhotographer("sign-up-photographer")
}