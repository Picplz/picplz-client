package com.hm.picplz.domain.model

data class PortfolioSummary(
    val id: Long,
    val representativeImage: String?,
    val photoCount: Int,
    val location: String?,
    val uploadDate: String?,
)
