package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.PortfolioListResponseDto
import com.hm.picplz.data.model.PortfolioResponseDto
import com.hm.picplz.data.source.PortfolioSource
import javax.inject.Inject

interface PortfolioService {
    suspend fun getPortfoliosByPhotographer(
        photographerId: Long,
        page: Int = 0,
        size: Int = 9,
    ): AppResult<PortfolioListResponseDto>

    suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioResponseDto>
}

class PortfolioServiceImpl
    @Inject
    constructor(
        private val portfolioSource: PortfolioSource,
    ) : PortfolioService {
        override suspend fun getPortfoliosByPhotographer(
            photographerId: Long,
            page: Int,
            size: Int,
        ): AppResult<PortfolioListResponseDto> =
            portfolioSource.getPortfoliosByPhotographer(
                photographerId = photographerId,
                page = page,
                size = size,
            )

        override suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioResponseDto> =
            portfolioSource.getPortfolio(portfolioId)
    }
