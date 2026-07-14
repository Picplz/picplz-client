package com.hm.picplz.data.api

import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.MemberInfoResponseDto
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.model.UpdateMemberLocationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberApi {
    @GET("api/v1/members/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): Response<Unit>

    @GET("api/v1/members/{memberId}/info")
    suspend fun getMemberInfo(
        @Path("memberId") memberId: Long,
    ): Response<ApiResponse<MemberInfoResponseDto>>

    @PATCH("api/v1/members/info")
    suspend fun updateMemberInfo(
        @Body request: UpdateMemberInfoRequest,
    ): Response<Unit>

    @POST("api/v1/members/location")
    suspend fun updateMemberLocation(
        @Body request: UpdateMemberLocationRequest,
    ): Response<Unit>
}
