package com.hm.picplz.ui.screen.sign_up.sign_up_client

import com.hm.picplz.data.model.User
import com.hm.picplz.viewmodel.emptyUserData

data class SignUpClientState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val userInfo: User = emptyUserData,
){
    companion object {
        fun idle(): SignUpClientState {
            return SignUpClientState(
                isLoading = false,
                error = null,
                userInfo = emptyUserData,
            )
        }
    }
}
