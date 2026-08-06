package com.hm.picplz.data.source

import com.hm.picplz.data.api.ProductApi
import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.CreateProductRequest
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ProductIdResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class ProductSourceTest {
    @Test
    fun `photographer products unwraps runtime ApiResponse data`() =
        runTest {
            val product =
                ProductDto(
                    productId = 17L,
                    photographerId = 7L,
                    title = null,
                    name = "남친생기는 프사",
                    price = null,
                    shootPrice = 9_900,
                    description = null,
                    shootingTime = null,
                    shootDuration = 15,
                    imageUrl = null,
                    otherDetails = "아이폰 촬영",
                    productPhotos = emptyList(),
                )
            val source =
                ProductSourceImpl(
                    FakeProductApi(
                        productsResponse =
                            Response.success(
                                ApiResponse(
                                    timestamp = "2026-07-27T00:00:00",
                                    statusCode = 200,
                                    message = "success",
                                    data = listOf(product),
                                ),
                            ),
                    ),
                )

            val products = source.getPhotographerProducts(7L).getOrThrow()

            assertEquals(listOf(product), products)
        }
}

private class FakeProductApi(
    private val productsResponse: Response<ApiResponse<List<ProductDto>>>,
) : ProductApi {
    override suspend fun getPhotographerProducts(photographerId: Long): Response<ApiResponse<List<ProductDto>>> =
        productsResponse

    override suspend fun createProduct(request: CreateProductRequest): Response<ProductIdResponse> = error("Not used")
}
