package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.User

sealed class SignUpPhotographerIntent {
    data class SetUserInfo(val userInfo: User) : SignUpPhotographerIntent()
    data object NavigateToPrev : SignUpPhotographerIntent()
    data class SetPhotographyExperience(val hasExperience: Boolean) : SignUpPhotographerIntent()
}