package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.net.Uri
import com.hm.picplz.data.model.UserType

sealed class SignUpCommonIntent {
    data object NavigateToPrev : SignUpCommonIntent()
    data class ClickUserTypeButton(val userType : UserType) : SignUpCommonIntent()
    data object NavigateToSelected : SignUpCommonIntent()
    data object ResetSelectedUserType : SignUpCommonIntent()
    data class SetNickname(val newNickname: String) : SignUpCommonIntent()
    data class SetProfileImageUri(val newProfileImageUri: Uri?) : SignUpCommonIntent()
    data class Navigate(val destination: String) : SignUpCommonIntent()
    data object ShowFileUploadDialog : SignUpCommonIntent()
    data object ToggleValidateDialog : SignUpCommonIntent()
}
