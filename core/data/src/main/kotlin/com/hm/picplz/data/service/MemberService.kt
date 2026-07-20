package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.MemberInfoResponseDto
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.model.UpdateMemberLocationRequest
import com.hm.picplz.data.model.toDomain
import com.hm.picplz.data.source.MemberSource
import com.hm.picplz.domain.model.MemberProfile
import javax.inject.Inject

interface MemberService {
    suspend fun checkNicknameAvailable(nickname: String): AppResult<Boolean>

    suspend fun getMemberInfo(memberId: Long): AppResult<MemberProfile>

    suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): AppResult<Unit>

    suspend fun updateMemberLocation(request: UpdateMemberLocationRequest): AppResult<Unit>
}

class MemberServiceImpl
    @Inject
    constructor(
        private val memberSource: MemberSource,
    ) : MemberService {
        override suspend fun checkNicknameAvailable(nickname: String): AppResult<Boolean> =
            memberSource.checkNickname(nickname)

        override suspend fun getMemberInfo(memberId: Long): AppResult<MemberProfile> =
            memberSource.getMemberInfo(memberId).map(MemberInfoResponseDto::toDomain)

        override suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): AppResult<Unit> =
            memberSource.updateMemberInfo(request)

        override suspend fun updateMemberLocation(request: UpdateMemberLocationRequest): AppResult<Unit> =
            memberSource.updateMemberLocation(request)
    }
