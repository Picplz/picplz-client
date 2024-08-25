package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.UserType

sealed class SignUpIntent {
    data object NavigateToPrev : SignUpIntent()
    data class ClickUserTypeButton(val userType : UserType) : SignUpIntent()
    data object NavigateToSelected : SignUpIntent()
    data object ResetSelectedUserType : SignUpIntent()
}
