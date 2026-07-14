package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.api.PortfolioApi
import com.hm.picplz.data.model.PortfolioListResponseDto
import com.hm.picplz.data.util.safeApiCall
import javax.inject.Inject

interface PortfolioSource {
    suspend fun getPortfoliosByPhotographer(
        photographerId: Long,
        page: Int = 0,
        size: Int = 9,
    ): AppResult<PortfolioListResponseDto>
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
            safeApiCall {
                portfolioApi.getPortfoliosByPhotographer(
                    photographerId = photographerId,
                    page = page,
                    size = size,
                )
            }
    }
