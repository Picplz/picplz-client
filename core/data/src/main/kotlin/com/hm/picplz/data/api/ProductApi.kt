package com.hm.picplz.data.api

import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.CreateProductRequest
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ProductIdResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {
    @GET("api/v1/photographers/{photographerId}/products")
    suspend fun getPhotographerProducts(
        @Path("photographerId") photographerId: Long,
    ): Response<ApiResponse<List<ProductDto>>>

    @POST("api/v1/products")
    suspend fun createProduct(
        @Body request: CreateProductRequest,
    ): Response<ProductIdResponse>
}
