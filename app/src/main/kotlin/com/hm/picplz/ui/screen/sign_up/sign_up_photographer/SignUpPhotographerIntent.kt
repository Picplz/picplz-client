package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.net.Uri
import com.hm.picplz.data.model.User

sealed class SignUpPhotographerIntent {
    data class SetUserInfo(val userInfo: User) : SignUpPhotographerIntent()
    data class SetNickname(val newNickname: String) : SignUpPhotographerIntent()
    data class SetProfileImageUri(val newProfileImageUri: Uri?) : SignUpPhotographerIntent()
    data object NavigateToPrev : SignUpPhotographerIntent()
    data class ChangeStep(val stepNum: Int) : SignUpPhotographerIntent()
    data object ClickSubmitButton : SignUpPhotographerIntent()
}