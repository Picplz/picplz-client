package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateMemberLocationUseCase
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
    ) {
        suspend operator fun invoke(
            memberId: Long,
            location: LocationCoordinate,
        ): AppResult<Unit> =
            memberRepository.updateMemberLocation(
                memberId = memberId,
                location = location,
            )
    }
