package com.hm.picplz.ui.screen.sign_up_client

import android.net.Uri
import com.hm.picplz.data.model.User

sealed class SignUpClientIntent {
    data class SetUserInfo(val userInfo: User) : SignUpClientIntent()
    data class SetNickName(val newNickname: String) : SignUpClientIntent()
    data class SetProfileImageUri(val newProfileImageUri: Uri?) : SignUpClientIntent()
    data object NavigateToPrev : SignUpClientIntent()
    data class ChangeStep(val stepNum: Int) : SignUpClientIntent()
}