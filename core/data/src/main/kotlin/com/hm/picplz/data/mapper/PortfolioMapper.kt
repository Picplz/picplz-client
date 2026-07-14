package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.PortfolioSummaryResponseDto
import com.hm.picplz.domain.model.PortfolioSummary

fun PortfolioSummaryResponseDto.toDomain(): PortfolioSummary =
    PortfolioSummary(
        id = portfolioId ?: 0L,
        representativeImage = representativeImage,
        photoCount = photoCount ?: 0,
        location = location,
        uploadDate = uploadDate,
    )
