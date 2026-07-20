package com.hm.picplz.di

import com.hm.picplz.data.repository.PortfolioRepositoryImpl
import com.hm.picplz.data.service.PortfolioService
import com.hm.picplz.data.service.PortfolioServiceImpl
import com.hm.picplz.data.source.PortfolioSource
import com.hm.picplz.data.source.PortfolioSourceImpl
import com.hm.picplz.domain.repository.PortfolioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PortfolioModule {
    @Binds
    @Singleton
    abstract fun bindPortfolioRepository(portfolioRepositoryImpl: PortfolioRepositoryImpl): PortfolioRepository

    @Binds
    @Singleton
    abstract fun bindPortfolioService(portfolioServiceImpl: PortfolioServiceImpl): PortfolioService

    @Binds
    @Singleton
    abstract fun bindPortfolioSource(portfolioSourceImpl: PortfolioSourceImpl): PortfolioSource
}
