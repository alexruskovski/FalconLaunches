package com.alexruskovski.falcon.di

import android.content.Context
import com.alexruskovski.falcon.FalconApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by Alexander Ruskovski on 10/09/2021
 */

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext app: Context) = app as FalconApp
}