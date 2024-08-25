package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.UserType

data class SignUpState(
    val selectedUserType: UserType? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    companion object {
        fun idle(): SignUpState {
            return SignUpState(
                selectedUserType = null,
                isLoading = false,
                error = null
            )
        }
    }
}