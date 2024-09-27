package com.hm.picplz.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String
) : Parcelable


enum class UserType {
    User,
    Photographer
}

enum class SelectionState {
    UNSELECTED, SELECTED, DESELECTED
}