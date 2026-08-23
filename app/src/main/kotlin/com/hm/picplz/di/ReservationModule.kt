package com.hm.picplz.di

import com.hm.picplz.data.api.ReservationApi
import com.hm.picplz.data.service.ReservationService
import com.hm.picplz.data.service.ReservationServiceImpl
import com.hm.picplz.data.source.ReservationSource
import com.hm.picplz.data.source.ReservationSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReservationModule {
    @Binds
    @Singleton
    abstract fun bindReservationService(reservationServiceImpl: ReservationServiceImpl): ReservationService

    @Binds
    @Singleton
    abstract fun bindReservationSource(reservationSourceImpl: ReservationSourceImpl): ReservationSource

    companion object {
        /**
         * 다른 Api 는 `NetworkModule` 에서 제공하지만 이 모듈에 둡니다.
         * `NetworkModule` 은 @Provides 가 14개라, 하나 더 넣으면 detekt `TooManyFunctions`
         * (thresholdInObjects = 15) 한계에 정확히 걸려 다음 API 추가가 막힙니다.
         */
        @Provides
        @Singleton
        fun provideReservationApi(
            @PicplzApi retrofit: Retrofit,
        ): ReservationApi = retrofit.create(ReservationApi::class.java)
    }
}
