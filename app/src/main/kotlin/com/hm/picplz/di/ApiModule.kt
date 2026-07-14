package com.hm.picplz.di

import com.hm.picplz.BuildConfig
import com.hm.picplz.data.api.AddressApi
import com.hm.picplz.data.api.AuthApi
import com.hm.picplz.data.api.CameraApi
import com.hm.picplz.data.api.CustomerApi
import com.hm.picplz.data.api.KakaoMapApi
import com.hm.picplz.data.api.MemberApi
import com.hm.picplz.data.api.PhotographerApi
import com.hm.picplz.data.api.PortfolioApi
import com.hm.picplz.data.api.ProductApi
import com.hm.picplz.data.api.S3Api
import com.hm.picplz.data.provider.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PicplzApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            redactHeader("Authorization")
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @KakaoRetrofit
    fun provideKakaoRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoMapApi(
        @KakaoRetrofit retrofit: Retrofit,
    ): KakaoMapApi {
        return retrofit.create(KakaoMapApi::class.java)
    }

    @Provides
    @Singleton
    @PicplzApi
    fun providePicplzRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePhotographerApi(
        @PicplzApi retrofit: Retrofit,
    ): PhotographerApi {
        return retrofit.create(PhotographerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApi(
        @PicplzApi retrofit: Retrofit,
    ): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun providePortfolioApi(
        @PicplzApi retrofit: Retrofit,
    ): PortfolioApi {
        return retrofit.create(PortfolioApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAddressApi(
        @PicplzApi retrofit: Retrofit,
    ): AddressApi {
        return retrofit.create(AddressApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @PicplzApi retrofit: Retrofit,
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApi(
        @PicplzApi retrofit: Retrofit,
    ): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCameraApi(
        @PicplzApi retrofit: Retrofit,
    ): CameraApi {
        return retrofit.create(CameraApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCustomerApi(
        @PicplzApi retrofit: Retrofit,
    ): CustomerApi {
        return retrofit.create(CustomerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideS3Api(
        @PicplzApi retrofit: Retrofit,
    ): S3Api {
        return retrofit.create(S3Api::class.java)
    }
}
