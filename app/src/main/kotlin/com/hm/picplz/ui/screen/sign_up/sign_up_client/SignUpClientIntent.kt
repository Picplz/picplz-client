package com.hm.picplz.ui.screen.sign_up.sign_up_client

import android.net.Uri
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent

sealed class SignUpClientIntent {
    data class SetUserInfo(val userInfo: User) : SignUpClientIntent()
    data object NavigateToPrev : SignUpClientIntent()
}