package com.hm.picplz.ui.screen.sign_up_client

import android.net.Uri

sealed class SignUpClientSideEffect {
    data class SubmitProfileInfo(
        val nickname: String,
        val profileImageUrl: Uri?,
    ) : SignUpClientSideEffect()
    data object ShowFileUploadDialog : SignUpClientSideEffect()
    data object NavigateToPrev : SignUpClientSideEffect()
}