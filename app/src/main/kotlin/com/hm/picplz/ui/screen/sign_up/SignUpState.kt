package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.UserType

sealed class SignUpState {
    data class SelectedUserType(val userType: UserType)
}