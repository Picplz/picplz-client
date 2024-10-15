package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.data.model.PhotographyExperience
import com.hm.picplz.data.model.User
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.viewmodel.emptyUserData

data class SignUpPhotographerState(
    val currentStep: Int? = 0,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val userInfo: User = emptyUserData,
    val hasPhotographyExperience: Boolean? = null,
    val photographyExperience: PhotographyExperience? = null,
    val experienceChipList: List<ChipItem> = defaultChipList()
) {
    companion object {
        private fun defaultChipList(): List<ChipItem> {
            return listOf(
                ChipItem(id = "1", label = "MZ 감성"),
                ChipItem(id = "2", label = "을지로 감성"),
                ChipItem(id = "3", label = "키치 감성"),
                ChipItem(id = "4", label = "퇴폐 감성"),
                ChipItem(id = "5", label = "올드머니 감성")
            )
        }

        fun idle(): SignUpPhotographerState {
            return SignUpPhotographerState(
                currentStep = 0,
                isLoading = false,
                error = null,
                userInfo = emptyUserData,
                hasPhotographyExperience = null,
                photographyExperience = null,
                experienceChipList = defaultChipList()
            )
        }
    }
}
