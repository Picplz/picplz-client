package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.PortfolioDetail
import com.hm.picplz.domain.repository.PortfolioRepository
import javax.inject.Inject

class GetPortfolioUseCase
    @Inject
    constructor(
        private val portfolioRepository: PortfolioRepository,
    ) {
        suspend operator fun invoke(portfolioId: Long): AppResult<PortfolioDetail> =
            portfolioRepository.getPortfolio(portfolioId)
    }
