package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.CreateCustomerRequest
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotoMoodRequest
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.model.UpdateActiveAreaRequest
import com.hm.picplz.data.model.UpdateActiveAreaResponse
import com.hm.picplz.data.source.CustomerSource
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.domain.model.CustomerSignup
import com.hm.picplz.domain.model.PhotographerSignup
import com.hm.picplz.domain.model.PhotographerSignupActiveArea
import com.hm.picplz.domain.model.PhotographerSignupCamera
import com.hm.picplz.domain.model.PhotographerSignupCameraType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SignupServiceMappingTest {
    @Test
    fun `customer service maps domain signup to create customer request`() =
        runTest {
            val source = FakeCustomerSource()
            val service = CustomerServiceImpl(source)

            service.createCustomer(
                CustomerSignup(
                    nickname = "고객",
                    socialEmail = "customer@example.com",
                    socialProvider = "KAKAO",
                    socialCode = "CUSTOMER_SOCIAL_CODE",
                    profileImage = null,
                ),
            )

            val request = source.lastRequest ?: error("customer request should be submitted")

            assertEquals("고객", request.nickname)
            assertEquals("customer@example.com", request.socialEmail)
            assertEquals("KAKAO", request.socialProvider)
            assertEquals("CUSTOMER_SOCIAL_CODE", request.socialCode)
            assertEquals(null, request.profileImage)
        }

    @Test
    fun `photographer service maps domain signup to create photographer request`() =
        runTest {
            val source = FakePhotographerSource()
            val service = PhotographerServiceImpl(source)

            service.createPhotographer(
                PhotographerSignup(
                    nickname = "작가",
                    socialEmail = "photographer@example.com",
                    socialProvider = "KAKAO",
                    socialCode = "PHOTOGRAPHER_SOCIAL_CODE",
                    profileImage = "profile/object-key.jpg",
                    photoMoods = listOf("청량", "심플"),
                    activeAreas =
                        listOf(
                            PhotographerSignupActiveArea(code = 110L, priority = 1),
                            PhotographerSignupActiveArea(code = 220L, priority = 2),
                        ),
                    cameras =
                        listOf(
                            PhotographerSignupCamera(
                                type = PhotographerSignupCameraType.PHONE,
                                brand = "애플",
                                name = "아이폰 15",
                                cameraType = null,
                            ),
                            PhotographerSignupCamera(
                                type = PhotographerSignupCameraType.CAMERA,
                                brand = "소니",
                                name = "A7C II",
                                cameraType = "미러리스 카메라",
                            ),
                        ),
                ),
            )

            val request = source.lastRequest ?: error("photographer request should be submitted")

            assertEquals("작가", request.nickname)
            assertEquals("photographer@example.com", request.socialEmail)
            assertEquals("KAKAO", request.socialProvider)
            assertEquals("PHOTOGRAPHER_SOCIAL_CODE", request.socialCode)
            assertEquals("profile/object-key.jpg", request.profileImage)
            assertEquals(listOf("청량", "심플"), request.photoMoods)
            assertEquals(110L, request.activeAreas[0].code)
            assertEquals(1, request.activeAreas[0].priority)
            assertEquals(220L, request.activeAreas[1].code)
            assertEquals(2, request.activeAreas[1].priority)
            assertEquals("PHONE", request.cameras[0].type)
            assertEquals("애플", request.cameras[0].brand)
            assertEquals("아이폰 15", request.cameras[0].name)
            assertEquals(null, request.cameras[0].cameraType)
            assertEquals("CAMERA", request.cameras[1].type)
            assertEquals("소니", request.cameras[1].brand)
            assertEquals("A7C II", request.cameras[1].name)
            assertEquals("미러리스 카메라", request.cameras[1].cameraType)
        }

    private class FakeCustomerSource : CustomerSource {
        var lastRequest: CreateCustomerRequest? = null
            private set

        override suspend fun createCustomer(request: CreateCustomerRequest): AppResult<Unit> {
            lastRequest = request
            return Result.success(Unit)
        }
    }

    private class FakePhotographerSource : PhotographerSource {
        var lastRequest: CreatePhotographerRequest? = null
            private set

        override suspend fun createPhotographer(request: CreatePhotographerRequest): AppResult<Unit> {
            lastRequest = request
            return Result.success(Unit)
        }

        override suspend fun addPhotoMood(request: PhotoMoodRequest): AppResult<Unit> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun deletePhotoMood(request: PhotoMoodRequest): AppResult<Unit> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun updateActiveAreas(request: UpdateActiveAreaRequest): AppResult<UpdateActiveAreaResponse> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): AppResult<List<NearbyPhotographerCard>> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getPhotographerInfo(photographerId: Long): AppResult<PhotographerDetailDto> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getPhotographerReviews(
            photographerId: Long,
            page: Int,
            size: Int,
            sort: String,
        ): AppResult<ReviewListDto> {
            throw NotImplementedError("Not used in test")
        }
    }
}
