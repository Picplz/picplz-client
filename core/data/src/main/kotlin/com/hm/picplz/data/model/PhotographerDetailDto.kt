package com.hm.picplz.data.model

data class PhotographerDetailDto(
    val photographerId: Long,
    val nickname: String,
    val profileImage: String?,
    val area: List<ActiveAreaDto>?,
    val introduction: String?,
    val active: String,
    val instagram: String?,
    val photoMoods: List<String?>?,
    val cameras: List<CameraInfoDto>? = null,
    val followers: Int?,
    val isFollowing: String?,
)

data class ActiveAreaDto(
    val code: Long?,
    val name: String?,
    val priority: Int?,
)

data class PhotographerRatingDto(
    val averageRating: Float,
    val totalReviews: Long,
)

data class ReviewListDto(
    val averageRating: Float?,
    val totalReviews: Long?,
    val reviews: List<ReviewSummaryDto>?,
)

data class ReviewSummaryDto(
    val reviewId: Long,
    val customerId: Long?,
    val customerNickname: String?,
    val customerProfileImage: String?,
    val rating: Float?,
    val content: String?,
    val photos: List<ReviewPhotoDto>?,
    val createdAt: String?,
)

data class ReviewPhotoDto(
    val photoId: Long?,
    val imageUrl: String?,
    val photoOrder: Int?,
)
