package com.hm.picplz.data.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.mapper.toShootingPackage
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.data.service.ProductService
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerDetail
import com.hm.picplz.domain.repository.PhotographerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PhotographerRepositoryImpl
    @Inject
    constructor(
        private val photographerService: PhotographerService,
        private val productService: ProductService,
    ) : PhotographerRepository {
        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): AppResult<FilteredPhotographers> = photographerService.getNearbyPhotographers(longitude, latitude, distance)

        override suspend fun getPhotographerDetail(
            photographerId: Long,
            reviewSort: String,
        ): AppResult<PhotographerDetail> =
            coroutineScope {
                runCatchingAppError {
                    val profileDeferred = async { photographerService.getPhotographerInfo(photographerId) }
                    val reviewsDeferred =
                        async {
                            photographerService.getPhotographerReviews(
                                photographerId = photographerId,
                                sort = reviewSort,
                            )
                        }
                    val productsDeferred = async { productService.getPhotographerProducts(photographerId) }

                    val profileInfo = profileDeferred.await().getOrThrow()
                    val reviewData = reviewsDeferred.await().getOrThrow()
                    val shootingPackages = productsDeferred.await().getOrThrow().map { it.toShootingPackage() }

                    PhotographerDetail(
                        profileInfo = profileInfo,
                        reviewData = reviewData,
                        shootingPackages = shootingPackages,
                    )
                }
            }

        override suspend fun getPhotographerMoodKeywords(photographerId: Long): AppResult<List<String>> =
            photographerService.getPhotographerMoodKeywords(photographerId)

        override suspend fun addPhotoMood(photoMood: String): AppResult<Unit> =
            photographerService.addPhotoMood(photoMood)

        override suspend fun deletePhotoMood(photoMood: String): AppResult<Unit> =
            photographerService.deletePhotoMood(photoMood)

        override suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>> =
            photographerService.getActiveAreas(photographerId)

        override suspend fun updateActiveAreas(areas: List<Area>): AppResult<List<Area>> =
            photographerService.updateActiveAreas(areas)
    }
