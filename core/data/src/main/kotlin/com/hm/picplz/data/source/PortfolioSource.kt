package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.api.PortfolioApi
import com.hm.picplz.data.model.PortfolioListResponseDto
import com.hm.picplz.data.model.PortfolioResponseDto
import com.hm.picplz.data.util.safeApiCall
import javax.inject.Inject

interface PortfolioSource {
    suspend fun getPortfoliosByPhotographer(
        photographerId: Long,
        page: Int = 0,
        size: Int = 9,
    ): AppResult<PortfolioListResponseDto>

    suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioResponseDto>
}

class PortfolioSourceImpl
    @Inject
    constructor(
        private val portfolioApi: PortfolioApi,
    ) : PortfolioSource {
        override suspend fun getPortfoliosByPhotographer(
            photographerId: Long,
            page: Int,
            size: Int,
        ): AppResult<PortfolioListResponseDto> =
            safeApiCall(
                call = {
                    portfolioApi.getPortfoliosByPhotographer(
                        photographerId = photographerId,
                        page = page,
                        size = size,
                    )
                },
                transform = { it.data },
            )

        override suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioResponseDto> =
            safeApiCall(
                call = { portfolioApi.getPortfolio(portfolioId) },
                transform = { it.data },
            )
    }
