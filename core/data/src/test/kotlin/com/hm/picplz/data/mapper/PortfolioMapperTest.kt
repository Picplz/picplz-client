package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.PortfolioPhotoResponseDto
import com.hm.picplz.data.model.PortfolioResponseDto
import com.hm.picplz.data.model.PortfolioSummaryResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PortfolioMapperTest {
    @Test
    fun `restores an external image url wrapped as a signed S3 key`() {
        val portfolio =
            PortfolioSummaryResponseDto(
                portfolioId = 1L,
                representativeImage =
                    "https://bucket.s3.amazonaws.com/https%3A//picsum.photos/seed/port/1080/2640?signature=value",
                photoCount = 4,
                location = "서울 마포구 와우산로",
                uploadDate = "2026-05-10",
            )

        assertEquals(
            "https://picsum.photos/seed/port/1080/2640",
            portfolio.toDomain().representativeImage,
        )
    }

    @Test
    fun `keeps a normal signed image url unchanged`() {
        val imageUrl = "https://bucket.s3.amazonaws.com/portfolio/image.jpg?signature=value"
        val portfolio =
            PortfolioSummaryResponseDto(
                portfolioId = 1L,
                representativeImage = imageUrl,
                photoCount = 1,
                location = null,
                uploadDate = null,
            )

        assertEquals(imageUrl, portfolio.toDomain().representativeImage)
    }

    @Test
    fun `portfolio detail maps image urls in photo order`() {
        val portfolio =
            PortfolioResponseDto(
                portfolioId = 1L,
                photos =
                    listOf(
                        PortfolioPhotoResponseDto(2L, "second.jpg", 1),
                        PortfolioPhotoResponseDto(1L, "first.jpg", 0),
                    ),
                location = "서울 마포구",
                uploadDate = "2026-05-10",
                scrapCount = 0,
                scrapYN = false,
            )

        assertEquals(listOf("first.jpg", "second.jpg"), portfolio.toDomain().imageUris)
    }
}
