package com.hm.picplz.ui.screen.sign_up.sign_up_client

import android.net.Uri
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent

sealed class SignUpClientIntent {
    data class SetUserInfo(val userInfo: User) : SignUpClientIntent()
    data class SetNickname(val newNickname: String) : SignUpClientIntent()
    data class SetProfileImageUri(val newProfileImageUri: Uri?) : SignUpClientIntent()
    data object NavigateToPrev : SignUpClientIntent()
    data class ChangeStep(val stepNum: Int) : SignUpClientIntent()
    data object ClickSubmitButton : SignUpClientIntent()
}