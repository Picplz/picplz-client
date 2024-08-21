package com.hm.picplz.ui.screen.sign_up_client

sealed class SignUpClientSideEffect {
    data class SubmitProfileInfo(
        val nickname: String,
        val profileImageUrl: String,
    ) : SignUpClientSideEffect()
    data object ShowFileUploadDialog : SignUpClientSideEffect()
    data object NavigateToPrev : SignUpClientSideEffect()
}