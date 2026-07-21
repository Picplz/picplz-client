package com.hm.picplz.data.source

import com.hm.picplz.data.api.PortfolioApi
import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.PortfolioListResponseDto
import com.hm.picplz.data.model.PortfolioPhotoResponseDto
import com.hm.picplz.data.model.PortfolioResponseDto
import com.hm.picplz.data.model.PortfolioSummaryResponseDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class PortfolioSourceTest {
    @Test
    fun `portfolios unwraps runtime ApiResponse data`() =
        runTest {
            val api =
                FakePortfolioApi(
                    response =
                        Response.success(
                            ApiResponse(
                                timestamp = "2026-07-20T21:00:00",
                                statusCode = 200,
                                message = "success",
                                data =
                                    PortfolioListResponseDto(
                                        portfolios =
                                            listOf(
                                                PortfolioSummaryResponseDto(
                                                    portfolioId = 1L,
                                                    representativeImage = "portfolio.jpg",
                                                    photoCount = 4,
                                                    location = "서울 마포구 와우산로",
                                                    uploadDate = "2026-05-10",
                                                ),
                                            ),
                                        totalCount = 1L,
                                        totalPages = 1,
                                        currentPage = 0,
                                    ),
                            ),
                        ),
                )
            val source = PortfolioSourceImpl(api)

            val portfolio =
                source.getPortfoliosByPhotographer(
                    photographerId = 7L,
                    page = 0,
                    size = 1,
                ).getOrThrow().portfolios?.single()

            assertEquals("portfolio.jpg", portfolio?.representativeImage)
            assertEquals(4, portfolio?.photoCount)
        }

    @Test
    fun `portfolio detail unwraps runtime ApiResponse data`() =
        runTest {
            val api =
                FakePortfolioApi(
                    detailResponse =
                        Response.success(
                            ApiResponse(
                                timestamp = "2026-07-20T21:00:00",
                                statusCode = 200,
                                message = "success",
                                data =
                                    PortfolioResponseDto(
                                        portfolioId = 1L,
                                        photos =
                                            listOf(
                                                PortfolioPhotoResponseDto(
                                                    portfolioPhotoId = 1L,
                                                    image = "portfolio-1.jpg",
                                                    photoOrder = 0,
                                                ),
                                            ),
                                        location = "서울 마포구 와우산로",
                                        uploadDate = "2026-05-10",
                                        scrapCount = 0,
                                        scrapYN = false,
                                    ),
                            ),
                        ),
                )
            val source = PortfolioSourceImpl(api)

            val portfolio = source.getPortfolio(portfolioId = 1L).getOrThrow()

            assertEquals(1L, portfolio.portfolioId)
            assertEquals("portfolio-1.jpg", portfolio.photos?.single()?.image)
        }
}

private class FakePortfolioApi(
    private val response: Response<ApiResponse<PortfolioListResponseDto>>? = null,
    private val detailResponse: Response<ApiResponse<PortfolioResponseDto>>? = null,
) : PortfolioApi {
    override suspend fun getPortfoliosByPhotographer(
        photographerId: Long,
        page: Int,
        size: Int,
    ): Response<ApiResponse<PortfolioListResponseDto>> = checkNotNull(response)

    override suspend fun getPortfolio(portfolioId: Long): Response<ApiResponse<PortfolioResponseDto>> =
        checkNotNull(detailResponse)
}
