package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.PortfolioResponseDto
import com.hm.picplz.data.model.PortfolioSummaryResponseDto
import com.hm.picplz.domain.model.PortfolioDetail
import com.hm.picplz.domain.model.PortfolioSummary
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun PortfolioSummaryResponseDto.toDomain(): PortfolioSummary =
    PortfolioSummary(
        id = portfolioId ?: 0L,
        representativeImage = representativeImage?.restoreEmbeddedImageUrl(),
        photoCount = photoCount ?: 0,
        location = location,
        uploadDate = uploadDate,
    )

fun PortfolioResponseDto.toDomain(): PortfolioDetail =
    PortfolioDetail(
        id = portfolioId ?: 0L,
        imageUris =
            photos.orEmpty()
                .sortedBy { it.photoOrder }
                .mapNotNull { it.image?.restoreEmbeddedImageUrl() },
        location = location,
        uploadDate = uploadDate,
    )

private fun String.restoreEmbeddedImageUrl(): String {
    val encodedImageUrl =
        substringBefore('?')
            .substringAfterLast("/http", missingDelimiterValue = "")
            .takeIf { it.isNotEmpty() }
            ?.let { "http$it" }
            ?: return this

    return URLDecoder.decode(encodedImageUrl, StandardCharsets.UTF_8.name())
}
