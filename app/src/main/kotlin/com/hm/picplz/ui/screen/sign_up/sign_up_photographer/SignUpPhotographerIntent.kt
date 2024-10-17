package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.PhotographyExperience
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent

sealed class SignUpPhotographerIntent {
    data class SetUserInfo(val userInfo: User) : SignUpPhotographerIntent()
    data object NavigateToPrev : SignUpPhotographerIntent()
    data class SetHasPhotographyExperience(val hasExperience: Boolean) : SignUpPhotographerIntent()
    data class SetPhotographyExperience(val photographyExperienceId: String?) : SignUpPhotographerIntent()
    data class Navigate(val destination: String) : SignUpPhotographerIntent()
    data class SetEditingChipId(val chipId: String?) : SignUpPhotographerIntent()
    data class AddVibeChip(val label: String) : SignUpPhotographerIntent()
    data class DeleteVibeChip(val chipId: String) : SignUpPhotographerIntent()
    data class UpdateVibeChip(val chipId: String, val label: String) : SignUpPhotographerIntent()
}