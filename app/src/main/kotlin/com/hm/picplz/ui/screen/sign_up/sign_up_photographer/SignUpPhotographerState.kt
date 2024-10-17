package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.data.model.PhotographyExperience
import com.hm.picplz.data.model.User
import com.hm.picplz.viewmodel.emptyUserData

data class SignUpPhotographerState(
    val currentStep: Int? = 0,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val userInfo: User = emptyUserData,
    val hasPhotographyExperience: Boolean? = null,
    val photographyExperienceId: String? = null,
    val experienceChipList: List<ChipItem> = defaultExperienceChipList(),
    val vibeChipList: List<ChipItem> = defaultVibeChipList(),
    val editingChipId: String? = null,
) {
    companion object {
        private fun defaultExperienceChipList(): List<ChipItem> {
            return listOf(
                ChipItem(id = "1", label = "사진 전공"),
                ChipItem(id = "2", label = "수익 창출"),
                ChipItem(id = "3", label = "SNS 계정 운영"),
            )
        }

        private fun defaultVibeChipList(): List<ChipItem> {
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
                photographyExperienceId = null,
                experienceChipList = defaultExperienceChipList(),
                vibeChipList = defaultVibeChipList(),
                editingChipId = null,
            )
        }
    }
}
