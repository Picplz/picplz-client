package com.hm.picplz.data.api

import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotoMoodRequest
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.model.UpdateActiveAreaRequest
import com.hm.picplz.data.model.UpdateActiveAreaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotographerApi {
    @POST("api/v1/photographers")
    suspend fun createPhotographer(
        @Body request: CreatePhotographerRequest,
    ): Response<Unit>

    @POST("api/v1/photographers/photo-mood")
    suspend fun addPhotoMood(
        @Body request: PhotoMoodRequest,
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "api/v1/photographers/photo-mood", hasBody = true)
    suspend fun deletePhotoMood(
        @Body request: PhotoMoodRequest,
    ): Response<Unit>

    @PUT("api/v1/photographers/active-area")
    suspend fun updateActiveAreas(
        @Body request: UpdateActiveAreaRequest,
    ): Response<UpdateActiveAreaResponse>

    @GET("api/v1/members/location/nearby")
    suspend fun getNearbyPhotographers(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("distance") distance: Long,
    ): Response<ApiResponse<List<NearbyPhotographerCard>>>

    @GET("api/v1/photographers/{photographerId}/info")
    suspend fun getPhotographerInfo(
        @Path("photographerId") photographerId: Long,
    ): Response<ApiResponse<PhotographerDetailDto>>

    @GET("api/v1/photographers/{photographerId}/reviews")
    suspend fun getPhotographerReviews(
        @Path("photographerId") photographerId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "RECOMMENDED",
    ): Response<ApiResponse<ReviewListDto>>
}
