package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.os.Bundle

sealed class SignUpSideEffect {
    sealed class SelectUserTypeScreenSideEffect {
        data class NavigateToSelected(
            val destination: DestinationByUserType,
            val user: Bundle
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