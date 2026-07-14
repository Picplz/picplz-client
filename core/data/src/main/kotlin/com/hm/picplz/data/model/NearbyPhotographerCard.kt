package com.hm.picplz.data.model

data class NearbyPhotographerCard(
    val photographerId: Long,
    val nickname: String,
    val profileImage: String?,
    val active: String,
    val distance: Long,
    val photoMoods: List<String?>? = null,
    val activeAreas: List<String>? = null,
)
