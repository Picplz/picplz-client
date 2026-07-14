package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.PortfolioSummary
import com.hm.picplz.domain.repository.PortfolioRepository
import javax.inject.Inject

class GetPhotographerPortfoliosUseCase
    @Inject
    constructor(
        private val portfolioRepository: PortfolioRepository,
    ) {
        suspend operator fun invoke(
            photographerId: Long,
            page: Int = 0,
            size: Int = 9,
        ): AppResult<List<PortfolioSummary>> =
            portfolioRepository.getPhotographerPortfolios(
                photographerId = photographerId,
                page = page,
                size = size,
            )
    }
