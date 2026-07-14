package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class GetNearbyPhotographersUseCase
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
    ) {
        suspend operator fun invoke(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): AppResult<FilteredPhotographers> =
            photographerRepository.getNearbyPhotographers(
                longitude = longitude,
                latitude = latitude,
                distance = distance,
            )
    }
