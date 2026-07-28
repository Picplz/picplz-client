package com.hm.picplz.di

import com.hm.picplz.data.repository.PhotographerRepositoryImpl
import com.hm.picplz.data.service.PhotographerSearchService
import com.hm.picplz.data.service.PhotographerSearchServiceImpl
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.data.service.PhotographerServiceImpl
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.data.source.PhotographerSourceImpl
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.repository.PhotographerSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotographerModule {
    @Binds
    @Singleton
    abstract fun bindPhotographerService(photographerServiceImpl: PhotographerServiceImpl): PhotographerService

    @Binds
    @Singleton
    abstract fun bindPhotographerSearchService(
        photographerSearchServiceImpl: PhotographerSearchServiceImpl,
    ): PhotographerSearchService

    @Binds
    @Singleton
    abstract fun bindPhotographerSource(photographerSourceImpl: PhotographerSourceImpl): PhotographerSource

    @Binds
    @Singleton
    abstract fun bindPhotographerRepository(
        photographerRepositoryImpl: PhotographerRepositoryImpl,
    ): PhotographerRepository

    @Binds
    @Singleton
    abstract fun bindPhotographerSearchRepository(
        photographerRepositoryImpl: PhotographerRepositoryImpl,
    ): PhotographerSearchRepository
}
