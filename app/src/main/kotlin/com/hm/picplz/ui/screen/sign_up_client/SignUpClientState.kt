package com.hm.picplz.ui.screen.sign_up

sealed class SignUpClientState {
    data class Step(
        val currentStep : SignUpStep
    ) : SignUpState()
}

sealed interface SignUpStep {
    data class Nickname(val nickname: String) : SignUpStep
    data class ProfileImage(val imageUrl: String) : SignUpStep
}