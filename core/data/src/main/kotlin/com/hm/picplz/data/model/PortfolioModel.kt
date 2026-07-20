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

data class PortfolioResponseDto(
    val portfolioId: Long?,
    val photos: List<PortfolioPhotoResponseDto>?,
    val location: String?,
    val uploadDate: String?,
    val scrapCount: Long?,
    val scrapYN: Boolean?,
)

data class PortfolioPhotoResponseDto(
    val portfolioPhotoId: Long?,
    val image: String?,
    val photoOrder: Int?,
)
