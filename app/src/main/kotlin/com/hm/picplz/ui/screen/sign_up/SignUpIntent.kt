package com.hm.picplz.ui.screen.sign_up

import android.net.Uri
import com.hm.picplz.data.model.UserType

sealed class SignUpIntent {
    data object NavigateToPrev : SignUpIntent()
    data class ClickUserTypeButton(val userType : UserType) : SignUpIntent()
    data object NavigateToSelected : SignUpIntent()
    data object ResetSelectedUserType : SignUpIntent()
    data class SetNickname(val newNickname: String) : SignUpIntent()
    data class SetProfileImageUri(val newProfileImageUri: Uri?) : SignUpIntent()
    data class Navigate(val destination: String) : SignUpIntent()
}
