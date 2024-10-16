package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent

sealed class SignUpPhotographerIntent {
    data class SetUserInfo(val userInfo: User) : SignUpPhotographerIntent()
    data object NavigateToPrev : SignUpPhotographerIntent()
    data class SetPhotographyExperience(val hasExperience: Boolean) : SignUpPhotographerIntent()
    data class Navigate(val destination: String) : SignUpPhotographerIntent()
    data class SetEditingChipId(val chipId: String?) : SignUpPhotographerIntent()
    data class AddChip(val label: String) : SignUpPhotographerIntent()
    data class DeleteChip(val chipId: String) : SignUpPhotographerIntent()
}