package com.hm.picplz.ui.screen.sign_up

sealed class SignUpIntent {
    data class SelectUserType(val userType : UserType) : SignUpIntent()
    object NavigateToPrev : SignUpIntent()
}

enum class UserType {
    User,
    Photographer
}