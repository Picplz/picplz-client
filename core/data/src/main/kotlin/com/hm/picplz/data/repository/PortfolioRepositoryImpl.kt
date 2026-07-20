package com.hm.picplz.data.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.service.PortfolioService
import com.hm.picplz.domain.model.PortfolioSummary
import com.hm.picplz.domain.repository.PortfolioRepository
import javax.inject.Inject

class PortfolioRepositoryImpl
    @Inject
    constructor(
        private val portfolioService: PortfolioService,
    ) : PortfolioRepository {
        override suspend fun getPhotographerPortfolios(
            photographerId: Long,
            page: Int,
            size: Int,
        ): AppResult<List<PortfolioSummary>> =
            portfolioService.getPortfoliosByPhotographer(
                photographerId = photographerId,
                page = page,
                size = size,
            ).map { response ->
                response.portfolios.orEmpty().map { it.toDomain() }
            }
    }
