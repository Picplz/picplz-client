package com.hm.picplz.ui.screen.sign_up_client

import com.hm.picplz.data.model.User

sealed class SignUpClientIntent {
    data class SetUserInfo(val userInfo: User) : SignUpClientIntent()
    data class SetNickName(val newNickname: String) : SignUpClientIntent()
    data class SetProfileImageUrl(val newProfileImageUrl: String) : SignUpClientIntent()
    data object NavigateToPrev : SignUpClientIntent()
}