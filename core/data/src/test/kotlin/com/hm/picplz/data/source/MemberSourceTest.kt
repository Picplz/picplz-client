package com.hm.picplz.data.source

import android.util.Log
import com.hm.picplz.common.error.AppError
import com.hm.picplz.data.api.MemberApi
import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.MemberInfoResponseDto
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.model.UpdateMemberLocationRequest
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito
import retrofit2.Response

class MemberSourceTest {
    private lateinit var mockedLog: MockedStatic<Log>

    @Before
    fun setUp() {
        mockedLog = Mockito.mockStatic(Log::class.java)
        mockedLog
            .`when`<Int> { Log.d(anyString(), anyString(), any(Throwable::class.java)) }
            .thenReturn(0)
    }

    @After
    fun tearDown() {
        mockedLog.close()
    }

    @Test
    fun `checkNickname returns true when API succeeds`() =
        runTest {
            val source = MemberSourceImpl(FakeMemberApi(checkNicknameResponse = Response.success(Unit)))

            val available = source.checkNickname("picplz").getOrThrow()

            assertEquals(true, available)
        }

    @Test
    fun `checkNickname returns false on 409 duplicate response`() =
        runTest {
            val source =
                MemberSourceImpl(
                    FakeMemberApi(
                        checkNicknameResponse = Response.error(409, "".toResponseBody()),
                    ),
                )

            val available = source.checkNickname("duplicate").getOrThrow()

            assertEquals(false, available)
        }

    @Test
    fun `checkNickname returns http app error for non duplicate failures`() =
        runTest {
            val source =
                MemberSourceImpl(
                    FakeMemberApi(
                        checkNicknameResponse =
                            Response.error(
                                500,
                                """
                                {
                                  "statusCode": 500,
                                  "message": "server error",
                                  "timestamp": "2026-06-06T00:00:00"
                                }
                                """.trimIndent().toResponseBody(),
                            ),
                    ),
                )

            val error = source.checkNickname("picplz").exceptionOrNull()

            assertTrue(error is AppError.Network.Http)
            assertEquals(500, (error as AppError.Network.Http).code)
            assertEquals("server error", error.serverMessage)
        }

    @Test
    fun `getMemberInfo unwraps ApiResponse data`() =
        runTest {
            val source =
                MemberSourceImpl(
                    FakeMemberApi(
                        memberInfoResponse =
                            Response.success(
                                ApiResponse(
                                    timestamp = "2026-06-06T00:00:00",
                                    statusCode = 200,
                                    message = "success",
                                    data =
                                        MemberInfoResponseDto(
                                            id = 35L,
                                            nickname = "유가영",
                                            role = "ROLE_USER",
                                            socialEmail = "picplz@example.com",
                                            profileImage = "profile/object-key.jpg",
                                            socialProvider = "KAKAO",
                                            socialCode = "SOCIAL_CODE",
                                            introduction = "hello",
                                            instagram = "picplz",
                                        ),
                                ),
                            ),
                    ),
                )

            val member = source.getMemberInfo(35L).getOrThrow()

            assertEquals(35L, member.id)
            assertEquals("유가영", member.nickname)
            assertEquals("ROLE_USER", member.role)
            assertEquals("SOCIAL_CODE", member.socialCode)
        }

    @Test
    fun `getMemberInfo returns http app error for failed response`() =
        runTest {
            val source =
                MemberSourceImpl(
                    FakeMemberApi(
                        memberInfoResponse =
                            Response.error(
                                404,
                                """
                                {
                                  "statusCode": 404,
                                  "message": "member not found",
                                  "timestamp": "2026-06-06T00:00:00"
                                }
                                """.trimIndent().toResponseBody(),
                            ),
                    ),
                )

            val error = source.getMemberInfo(404L).exceptionOrNull()

            assertTrue(error is AppError.Network.Http)
            assertEquals(404, (error as AppError.Network.Http).code)
            assertEquals("member not found", error.serverMessage)
        }

    @Test
    fun `updateMemberLocation treats successful raw response as unit`() =
        runTest {
            val source = MemberSourceImpl(FakeMemberApi(updateLocationResponse = Response.success(Unit)))

            val result =
                source.updateMemberLocation(
                    UpdateMemberLocationRequest(
                        memberId = 3L,
                        latitude = 37.5665,
                        longitude = 126.978,
                    ),
                )

            assertTrue(result.isSuccess)
        }

    private class FakeMemberApi(
        private val checkNicknameResponse: Response<Unit> = Response.success(Unit),
        private val memberInfoResponse: Response<ApiResponse<MemberInfoResponseDto>>? = null,
        private val updateLocationResponse: Response<Unit> = Response.success(Unit),
    ) : MemberApi {
        override suspend fun checkNickname(nickname: String): Response<Unit> = checkNicknameResponse

        override suspend fun getMemberInfo(memberId: Long): Response<ApiResponse<MemberInfoResponseDto>> =
            memberInfoResponse ?: error("memberInfoResponse is required")

        override suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): Response<Unit> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun updateMemberLocation(request: UpdateMemberLocationRequest): Response<Unit> =
            updateLocationResponse
    }
}
