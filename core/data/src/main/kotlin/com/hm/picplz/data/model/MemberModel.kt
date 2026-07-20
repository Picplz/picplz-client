package com.hm.picplz.data.model

import com.hm.picplz.common.model.UserType
import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.model.UpdateMemberProfileCommand

data class UpdateMemberInfoRequest(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val introduction: String?,
    val instagram: String?,
)

data class UpdateMemberLocationRequest(
    val memberId: Long,
    val latitude: Double,
    val longitude: Double,
)

data class MemberInfoResponseDto(
    val id: Long,
    val nickname: String,
    val role: String?,
    val socialEmail: String?,
    val profileImage: String?,
    val socialProvider: String?,
    val socialCode: String?,
    val introduction: String? = null,
    val instagram: String? = null,
)

fun UpdateMemberProfileCommand.toRequest() =
    UpdateMemberInfoRequest(
        id = id,
        nickname = nickname,
        profileImage = profileImage,
        introduction = introduction,
        instagram = instagram,
    )

fun MemberInfoResponseDto.toDomain() =
    MemberProfile(
        id = id,
        nickname = nickname,
        profileImage = profileImage,
        introduction = introduction,
        instagram = instagram,
        userType = role.toUserType(),
    )

private fun String?.toUserType(): UserType =
    when (this?.uppercase()) {
        "ROLE_PHOTOGRAPHER", "PHOTOGRAPHER" -> UserType.Photographer
        else -> UserType.User
    }
