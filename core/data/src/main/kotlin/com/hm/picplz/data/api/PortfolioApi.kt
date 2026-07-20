package com.hm.picplz.data.api

import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.PortfolioListResponseDto
import com.hm.picplz.data.model.PortfolioResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PortfolioApi {
    @GET("api/v1/portfolios")
    suspend fun getPortfoliosByPhotographer(
        @Query("photographerId") photographerId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 9,
    ): Response<ApiResponse<PortfolioListResponseDto>>

    @GET("api/v1/portfolios/{portfolioId}")
    suspend fun getPortfolio(
        @Path("portfolioId") portfolioId: Long,
    ): Response<ApiResponse<PortfolioResponseDto>>
}
