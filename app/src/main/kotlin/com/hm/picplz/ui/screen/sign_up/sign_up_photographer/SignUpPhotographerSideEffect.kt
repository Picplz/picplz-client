package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.net.Uri


sealed class SignUpPhotographerSideEffect {
    data class  SubmitProfileInfo(
        val nickname: String,
        val profileImageUri: Uri?,
    ) : SignUpPhotographerSideEffect()
    data object ShowFileUploadDialog : SignUpPhotographerSideEffect()
    data object NavigateToPrev : SignUpPhotographerSideEffect()
}