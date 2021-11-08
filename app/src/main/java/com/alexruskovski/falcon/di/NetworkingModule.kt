package com.alexruskovski.falcon.di

import android.app.Application
import com.alexruskovski.falcon.BuildConfig
import com.alexruskovski.falcon.Constants
import com.alexruskovski.falcon.data.remote.api.Api
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Alexander Ruskovski on 28/06/2021
 */

@InstallIn(SingletonComponent::class)
@Module
class NetworkingModule {

    @Singleton
    @Provides
    fun providesRetrofit(okHttp: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttp)
            .build()
    }

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .build()

    @Provides
    @Singleton
    fun api(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    fun providesOKHTTP(
        application: Application
    ): OkHttpClient {
        val cacheSize: Long = 64 * 1024 * 1024 // 64 MBs
        val cache = Cache(application.cacheDir, cacheSize)
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        return builder.cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }


}