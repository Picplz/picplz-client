package com.hm.picplz.data.model
import android.net.Uri
import android.os.Parcelable
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val nickname: String?,
    val email: String?,
    val userType: UserType?,
    val profileImageUri: Uri?,
    val photographyExperience: PhotographyExperience?,
    val photographyVibes: List<String>?,
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
