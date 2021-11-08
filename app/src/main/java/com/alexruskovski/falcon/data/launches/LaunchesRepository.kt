package com.alexruskovski.falcon.data.launches

import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.model.Launch

/**
 * Created by Alexander Ruskovski on 15/08/2021
 *
 * Interface to the Launches data layer.
 */

interface LaunchesRepository {

    /**
     * Get all launches
     * */
    suspend fun getAllLaunches(): Result<List<Launch>>

    /**
     * Get a specific launch by Id
     * */
    suspend fun getSingleLaunchDetails(launchId: String): Result<Launch>

}