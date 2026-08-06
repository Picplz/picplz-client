package com.hm.picplz.data.mapper

import com.hm.picplz.common.model.PhotoReview
import com.hm.picplz.data.model.ActiveAreaResponse
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.PhotographerSearchItemDto
import com.hm.picplz.data.model.PhotographerSearchPageDto
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.model.ReviewPhotoDto
import com.hm.picplz.data.model.ReviewSummaryDto
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.domain.model.PhotographerPage
import com.hm.picplz.domain.model.PhotographerReview
import com.hm.picplz.domain.model.PhotographerReviewData
import com.hm.picplz.domain.model.PhotographerReviewSummary
import com.hm.picplz.domain.model.ShootingPackage

fun NearbyPhotographerCard.toDomain(): Photographer {
    return Photographer(
        id = photographerId,
        name = nickname,
        profileImageUri = profileImage,
        isActive = active == "Y",
        distance = distance,
        photoMoods = photoMoods.toNormalizedPhotoMoods(),
        activeAreas = activeAreas ?: emptyList(),
        instagram = null,
        equipment = emptyList(),
        portfolioPhotos = emptyList(),
    )
}

fun List<NearbyPhotographerCard>.toDomain(): List<Photographer> {
    return map { it.toDomain() }
}

fun PhotographerSearchPageDto.toDomain(): PhotographerPage {
    val currentPage = number ?: 0
    return PhotographerPage(
        photographers = content.orEmpty().mapNotNull(PhotographerSearchItemDto::toDomain),
        page = currentPage,
        hasNext = last?.not() ?: (currentPage + 1 < (totalPages ?: 0)),
    )
}

private fun PhotographerSearchItemDto.toDomain(): Photographer? {
    val id = photographerId ?: return null
    return Photographer(
        id = id,
        name = nickname.orEmpty(),
        profileImageUri = profileImage,
        isActive = isActive == "Y",
        distance = 0,
        photoMoods = photoMoods.orEmpty().mapNotNull { it?.takeUnless(String::isBlank) },
    )
}

fun PhotographerDetailDto.toPhotographerInfo(): PhotographerInfo {
    return PhotographerInfo(
        id = photographerId.toInt(),
        name = nickname,
        socialAccount = instagram,
        infoText = introduction ?: "",
        isActive = active == "Y",
        isBookable = active == "Y",
        isFollow = isFollowing == "Y",
        followCount = followers ?: 0,
        profileImageUri = profileImage ?: "",
        workingArea = area?.mapNotNull { it.name } ?: emptyList(),
        keyword = photoMoods.toNormalizedPhotoMoods(),
        equipment =
            cameras
                .orEmpty()
                .map { "${it.brand} ${it.name}".trim() }
                .filter(String::isNotBlank)
                .distinct(),
        photoPortfolios = emptyList(),
    )
}

private fun List<String?>?.toNormalizedPhotoMoods(): List<String> =
    orEmpty()
        .mapNotNull { mood ->
            mood
                ?.trim()
                ?.trimStart('#')
                ?.trim()
                ?.takeUnless(String::isBlank)
        }
        .distinct()

fun ReviewPhotoDto.toPhotoReview(reviewId: Long): PhotoReview {
    return PhotoReview(
        reviewId = reviewId.toInt(),
        photoReviewUri = imageUrl ?: "",
        index = photoOrder ?: 0,
    )
}

fun ReviewSummaryDto.toPhotographerReview(): PhotographerReview {
    val photoReviews = photos?.map { it.toPhotoReview(reviewId) } ?: emptyList()
    return PhotographerReview(
        reviewId = reviewId.toInt(),
        profileImageUri = customerProfileImage ?: "",
        nickname = customerNickname ?: "",
        rating = rating ?: 0f,
        createdAt = createdAt ?: "",
        isReported = false,
        photoReviews = photoReviews,
        photoReviewCount = photoReviews.size,
        option = "",
        location = "",
        reviewText = content ?: "",
        isRecommended = false,
        recommendationCount = 0,
    )
}

fun ReviewListDto.toReviewData(): PhotographerReviewData {
    val mappedReviews = reviews?.map { it.toPhotographerReview() } ?: emptyList()
    val allPhotoReviews =
        mappedReviews.flatMap { it.photoReviews }

    val summary =
        PhotographerReviewSummary(
            averageRating = averageRating ?: 0f,
            keywordBars = emptyList(),
            totalReviewCount = totalReviews?.toInt() ?: 0,
            totalPhotoReviewCount = allPhotoReviews.size,
            photoReviews = allPhotoReviews,
        )

    return PhotographerReviewData(
        summary = summary,
        reviews = mappedReviews,
    )
}

fun ProductDto.toShootingPackage(): ShootingPackage {
    return ShootingPackage(
        packageId = productId,
        title = title ?: name.orEmpty(),
        price = price ?: shootPrice ?: 0,
        imageUri = imageUrl ?: productPhotos?.firstOrNull().orEmpty(),
        shootingTime = shootingTime ?: shootDuration?.let { "${it}분" }.orEmpty(),
        description = description ?: otherDetails.orEmpty(),
    )
}

fun ActiveAreaResponse.toArea(): Area {
    return Area(
        id = code,
        name = name,
        dong = name,
        ri = null,
    )
}
