package com.hm.picplz.domain.model

data class PortfolioSummary(
    val id: Long,
    val representativeImage: String?,
    val photoCount: Int,
    val location: String?,
    val uploadDate: String?,
)

data class PortfolioDetail(
    val id: Long,
    val imageUris: List<String>,
    val location: String?,
    val uploadDate: String?,
)
