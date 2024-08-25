package com.hm.picplz.ui.screen.sign_up

import android.net.Uri
import com.hm.picplz.data.model.User
import com.hm.picplz.viewmodel.emptyUserData

data class SignUpClientState(
    val currentStep: Int? = 0,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val userInfo: User = emptyUserData,
    val nickname: String = "",
    val profileImageUri: Uri? = null
){
    companion object {
        fun idle(): SignUpClientState {
            return SignUpClientState(
                currentStep = 0,
                isLoading = false,
                error = null,
                userInfo = emptyUserData,
                nickname = "",
                profileImageUri = null
            )
        }
    }
}
