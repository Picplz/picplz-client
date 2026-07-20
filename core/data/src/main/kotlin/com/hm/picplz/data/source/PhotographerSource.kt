package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.api.PhotographerApi
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotoMoodRequest
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.model.UpdateActiveAreaRequest
import com.hm.picplz.data.model.UpdateActiveAreaResponse
import com.hm.picplz.data.util.safeApiCall
import com.hm.picplz.data.util.safeApiCallUnit
import javax.inject.Inject

interface PhotographerSource {
    suspend fun createPhotographer(request: CreatePhotographerRequest): AppResult<Unit>

    suspend fun addPhotoMood(request: PhotoMoodRequest): AppResult<Unit>

    suspend fun deletePhotoMood(request: PhotoMoodRequest): AppResult<Unit>

    suspend fun updateActiveAreas(request: UpdateActiveAreaRequest): AppResult<UpdateActiveAreaResponse>

    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<List<NearbyPhotographerCard>>

    suspend fun getPhotographerInfo(photographerId: Long): AppResult<PhotographerDetailDto>

    suspend fun getPhotographerReviews(
        photographerId: Long,
        page: Int = 0,
        size: Int = 10,
        sort: String = "RECOMMENDED",
    ): AppResult<ReviewListDto>
}

class PhotographerSourceImpl
    @Inject
    constructor(
        private val photographerApi: PhotographerApi,
    ) : PhotographerSource {
        override suspend fun createPhotographer(request: CreatePhotographerRequest): AppResult<Unit> =
            safeApiCallUnit { photographerApi.createPhotographer(request) }

        override suspend fun addPhotoMood(request: PhotoMoodRequest): AppResult<Unit> =
            safeApiCallUnit { photographerApi.addPhotoMood(request) }

        override suspend fun deletePhotoMood(request: PhotoMoodRequest): AppResult<Unit> =
            safeApiCallUnit { photographerApi.deletePhotoMood(request) }

        override suspend fun updateActiveAreas(request: UpdateActiveAreaRequest): AppResult<UpdateActiveAreaResponse> =
            safeApiCall { photographerApi.updateActiveAreas(request) }

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): AppResult<List<NearbyPhotographerCard>> =
            safeApiCall({ photographerApi.getNearbyPhotographers(longitude, latitude, distance) }) { it.data }

        override suspend fun getPhotographerInfo(photographerId: Long): AppResult<PhotographerDetailDto> =
            safeApiCall({ photographerApi.getPhotographerInfo(photographerId) }) { it.data }

        override suspend fun getPhotographerReviews(
            photographerId: Long,
            page: Int,
            size: Int,
            sort: String,
        ): AppResult<ReviewListDto> =
            safeApiCall({ photographerApi.getPhotographerReviews(photographerId, page, size, sort) }) { it.data }
    }
