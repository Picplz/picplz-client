package com.hm.picplz.ui.screen.sign_up.sign_up_client

import com.hm.picplz.data.model.User

sealed class SignUpClientIntent {
    data class SetUserInfo(val userInfo: User) : SignUpClientIntent()
    data object NavigateToPrev : SignUpClientIntent()
}