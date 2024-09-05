package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.net.Uri
import com.hm.picplz.data.model.User
import com.hm.picplz.viewmodel.emptyUserData

data class SignUpPhotographerState (
    val currentStep: Int? = 0,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val userInfo: User = emptyUserData,
) {
    companion object {
        fun idle(): SignUpPhotographerState {
            return SignUpPhotographerState(
                currentStep = 0,
                isLoading = false,
                error = null,
                userInfo = emptyUserData,
            )
        }
    }

}