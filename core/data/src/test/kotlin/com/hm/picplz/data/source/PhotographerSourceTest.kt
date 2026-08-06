package com.hm.picplz.data.source

import com.hm.picplz.data.api.PhotographerApi
import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotoMoodRequest
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.PhotographerSearchItemDto
import com.hm.picplz.data.model.PhotographerSearchPageDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.model.UpdateActiveAreaRequest
import com.hm.picplz.data.model.UpdateActiveAreaResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class PhotographerSourceTest {
    @Test
    fun `nearby photographers unwraps runtime ApiResponse data`() =
        runTest {
            val api =
                FakePhotographerApi(
                    nearbyResponse =
                        Response.success(
                            ApiResponse(
                                timestamp = "2026-07-06T22:18:38",
                                statusCode = 200,
                                message = "success",
                                data =
                                    listOf(
                                        NearbyPhotographerCard(
                                            photographerId = 7L,
                                            nickname = "유가영 작가",
                                            profileImage = "profile.jpg",
                                            active = "Y",
                                            distance = 120L,
                                            photoMoods = listOf("필름", null),
                                        ),
                                    ),
                            ),
                        ),
                )
            val source = PhotographerSourceImpl(api)

            val cards =
                source.getNearbyPhotographers(
                    longitude = 126.978,
                    latitude = 37.5665,
                    distance = 2_000L,
                ).getOrThrow()

            assertEquals(1, cards.size)
            assertEquals(7L, cards.first().photographerId)
            assertEquals("유가영 작가", cards.first().nickname)
        }

    @Test
    fun `photographer search unwraps paged runtime response`() =
        runTest {
            val api =
                FakePhotographerApi(
                    searchResponse =
                        Response.success(
                            ApiResponse(
                                timestamp = "2026-07-20T20:00:00",
                                statusCode = 200,
                                message = "success",
                                data =
                                    PhotographerSearchPageDto(
                                        content =
                                            listOf(
                                                PhotographerSearchItemDto(
                                                    photographerId = 7L,
                                                    nickname = "유가영 작가",
                                                    profileImage = "profile.jpg",
                                                    isActive = "Y",
                                                    photoMoods = listOf("필름"),
                                                ),
                                            ),
                                        number = 0,
                                        totalPages = 2,
                                        last = false,
                                    ),
                            ),
                        ),
                )
            val source = PhotographerSourceImpl(api)

            val page =
                source.searchPhotographers(
                    keyword = "",
                    sortType = "RATING",
                    page = 0,
                    size = 5,
                ).getOrThrow()

            assertEquals(0, page.number)
            assertEquals(false, page.last)
            assertEquals(7L, page.content?.single()?.photographerId)
        }
}

private class FakePhotographerApi(
    private val nearbyResponse: Response<ApiResponse<List<NearbyPhotographerCard>>>? = null,
    private val searchResponse: Response<ApiResponse<PhotographerSearchPageDto>>? = null,
) : PhotographerApi {
    override suspend fun createPhotographer(request: CreatePhotographerRequest): Response<Unit> = error("Not used")

    override suspend fun addPhotoMood(request: PhotoMoodRequest): Response<Unit> = error("Not used")

    override suspend fun deletePhotoMood(request: PhotoMoodRequest): Response<Unit> = error("Not used")

    override suspend fun updateActiveAreas(request: UpdateActiveAreaRequest): Response<UpdateActiveAreaResponse> =
        error("Not used")

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Response<ApiResponse<List<NearbyPhotographerCard>>> = checkNotNull(nearbyResponse)

    override suspend fun searchPhotographers(
        keyword: String,
        sortType: String,
        page: Int,
        size: Int,
    ): Response<ApiResponse<PhotographerSearchPageDto>> = checkNotNull(searchResponse)

    override suspend fun getPhotographerInfo(
        photographerId: Long,
    ): Response<com.hm.picplz.data.model.ApiResponse<PhotographerDetailDto>> = error("Not used")

    override suspend fun getPhotographerReviews(
        photographerId: Long,
        page: Int,
        size: Int,
        sort: String,
    ): Response<com.hm.picplz.data.model.ApiResponse<ReviewListDto>> = error("Not used")
}
