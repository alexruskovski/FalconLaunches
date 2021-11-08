package com.alexruskovski.falcon.di

import com.alexruskovski.falcon.data.crew.CrewRepository
import com.alexruskovski.falcon.data.crew.CrewRepositoryImpl
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetRockets
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetLaunches
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetSingleLaunchDetails
import com.alexruskovski.falcon.data.launches.LaunchesRepository
import com.alexruskovski.falcon.data.launches.LaunchesRepositoryImpl
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetCrew
import com.alexruskovski.falcon.data.rockets.RocketsRepository
import com.alexruskovski.falcon.data.rockets.RocketsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Alexander Ruskovski on 28/06/2021
 */

@InstallIn(SingletonComponent::class)
@Module
class ReposModule {

    @Provides
    @Singleton
    fun providesLaunchesRepository(
        ucGetLaunches: UCGetLaunches,
        ucGetSingleLaunchDetails: UCGetSingleLaunchDetails
    ): LaunchesRepository {
        return LaunchesRepositoryImpl(
            ucGetLaunches,
            ucGetSingleLaunchDetails
        )
    }

    @Provides
    @Singleton
    fun providesRocketsRepository(
        ucGetRockets: UCGetRockets
    ): RocketsRepository {
        return RocketsRepositoryImpl(ucGetRockets)
    }

    @Provides
    @Singleton
    fun providesCrewRepos(
        ucGetCrew: UCGetCrew
    ): CrewRepository{
        return CrewRepositoryImpl(ucGetCrew)
    }

}