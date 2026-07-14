package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.MemberApi
import com.hm.picplz.data.model.MemberInfoResponseDto
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.model.UpdateMemberLocationRequest
import com.hm.picplz.data.util.safeApiCall
import com.hm.picplz.data.util.safeApiCallUnit
import com.hm.picplz.data.util.toHttpAppError
import javax.inject.Inject

interface MemberSource {
    suspend fun checkNickname(nickname: String): AppResult<Boolean>

    suspend fun getMemberInfo(memberId: Long): AppResult<MemberInfoResponseDto>

    suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): AppResult<Unit>

    suspend fun updateMemberLocation(request: UpdateMemberLocationRequest): AppResult<Unit>
}

class MemberSourceImpl
    @Inject
    constructor(
        private val memberApi: MemberApi,
    ) : MemberSource {
        override suspend fun checkNickname(nickname: String): AppResult<Boolean> =
            runCatchingAppError {
                val response = memberApi.checkNickname(nickname)
                when {
                    response.isSuccessful -> true
                    response.code() == 409 -> false
                    else -> throw response.toHttpAppError()
                }
            }

        override suspend fun getMemberInfo(memberId: Long): AppResult<MemberInfoResponseDto> =
            safeApiCall({ memberApi.getMemberInfo(memberId) }) { it.data }

        override suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): AppResult<Unit> =
            safeApiCallUnit { memberApi.updateMemberInfo(request) }

        override suspend fun updateMemberLocation(request: UpdateMemberLocationRequest): AppResult<Unit> =
            safeApiCallUnit { memberApi.updateMemberLocation(request) }
    }
