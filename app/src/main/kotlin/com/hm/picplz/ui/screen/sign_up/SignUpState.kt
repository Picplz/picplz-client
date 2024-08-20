package com.hm.picplz.ui.screen.sign_up

import com.hm.picplz.data.model.UserType

data class SignUpViewState(
    val selectedUserType: UserType? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    companion object {
        fun idle(): SignUpViewState {
            return SignUpViewState(
                selectedUserType = null,
                isLoading = false,
                error = null
            )
        }
    }
}