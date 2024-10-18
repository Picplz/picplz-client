package com.hm.picplz.data.model
import android.net.Uri
import android.os.Parcelable
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val nickname: String? = null,
    val email: String? = null,
    val userType: UserType? = null,
    val profileImageUri: Uri? = null,
    val photographyExperience: PhotographyExperience? = null,
    val photographyVibes: List<String>? = null,
) : Parcelable


enum class UserType {
    User,
    Photographer
}

enum class SelectionState {
    UNSELECTED, SELECTED, DESELECTED
}

enum class PhotographyExperience {
    PHOTO_MAJOR,
    INCOME_GENERATION,
    SNS_OPERATION,
}
