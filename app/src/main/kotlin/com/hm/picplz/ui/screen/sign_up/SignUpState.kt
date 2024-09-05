package com.hm.picplz.ui.screen.sign_up

import android.net.Uri
import com.hm.picplz.data.model.UserType

data class SignUpState(
    val currentStep: Int = 0,
    val selectedUserType: UserType? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val nickname: String = "",
    val profileImageUri: Uri? = null
) {
    companion object {
        fun idle(): SignUpState {
            return SignUpState(
                currentStep = 0,
                selectedUserType = null,
                isLoading = false,
                error = null,
                nickname = "",
                profileImageUri = null,
            )
        }
    }
}