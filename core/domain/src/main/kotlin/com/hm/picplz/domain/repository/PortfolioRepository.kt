package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.PortfolioSummary

interface PortfolioRepository {
    suspend fun getPhotographerPortfolios(
        photographerId: Long,
        page: Int = 0,
        size: Int = 9,
    ): AppResult<List<PortfolioSummary>>
}
