package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.User

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
    data class UpdateSelectedVibeChipList(val chipId: String, val label: String) : SignUpPhotographerIntent()
    data object SetUserPhotographyExperience : SignUpPhotographerIntent()
    data class NavigateWithSubmit(val destination: String) : SignUpPhotographerIntent()
    data object SetUserPhotographyVibe: SignUpPhotographerIntent()
    data class SetIsOpenDialog(val isOpen: Boolean) : SignUpPhotographerIntent()
}