package com.hm.picplz.data.model

data class PhotographerSearchPageDto(
    val content: List<PhotographerSearchItemDto>?,
    val number: Int?,
    val totalPages: Int?,
    val last: Boolean?,
)

data class PhotographerSearchItemDto(
    val photographerId: Long?,
    val nickname: String?,
    val profileImage: String?,
    val isActive: String?,
    val photoMoods: List<String?>?,
)
