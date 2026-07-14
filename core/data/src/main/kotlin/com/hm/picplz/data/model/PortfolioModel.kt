package com.hm.picplz.data.model

data class PortfolioListResponseDto(
    val portfolios: List<PortfolioSummaryResponseDto>?,
    val totalCount: Long?,
    val totalPages: Int?,
    val currentPage: Int?,
)

data class PortfolioSummaryResponseDto(
    val portfolioId: Long?,
    val representativeImage: String?,
    val photoCount: Int?,
    val location: String?,
    val uploadDate: String?,
)
