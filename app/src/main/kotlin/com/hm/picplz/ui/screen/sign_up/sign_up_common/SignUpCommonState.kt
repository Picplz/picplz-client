package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.net.Uri
import com.hm.picplz.data.model.NicknameFieldError
import com.hm.picplz.data.model.UserType

data class SignUpCommonState(
    val currentStep: Int = 0,
    val selectedUserType: UserType? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val nickname: String = "",
    val profileImageUri: Uri? = null,
    val nicknameFieldErrors: List<NicknameFieldError> = emptyList(),
    val showValidateDialog: Boolean = false,
) {
    companion object {
        fun idle(): SignUpCommonState {
            return SignUpCommonState(
                currentStep = 0,
                selectedUserType = null,
                isLoading = false,
                error = null,
                nickname = "",
                profileImageUri = null,
                nicknameFieldErrors = emptyList(),
                showValidateDialog = false,
            )
        }
    }
}