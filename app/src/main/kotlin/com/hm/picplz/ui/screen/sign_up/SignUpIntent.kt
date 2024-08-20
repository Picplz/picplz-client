package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.UserType

sealed class SignUpIntent {
    object NavigateToPrev : SignUpIntent()
    data class ClickUserTypeButton(val userType : UserType) : SignUpIntent()
    object NavigateToSelected : SignUpIntent()
}
