package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.os.Bundle


sealed class SignUpPhotographerSideEffect {
    data object NavigateToPrev : SignUpPhotographerSideEffect()
    data class  Navigate(val destination: String) : SignUpPhotographerSideEffect()
    data class  NavigateWithSubmit(val destination: String, val userInfo: Bundle) : SignUpPhotographerSideEffect()
}