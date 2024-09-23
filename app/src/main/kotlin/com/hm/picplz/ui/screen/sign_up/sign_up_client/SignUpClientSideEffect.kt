package com.hm.picplz.ui.screen.sign_up.sign_up_client

sealed class SignUpClientSideEffect {
    data object NavigateToPrev : SignUpClientSideEffect()
}