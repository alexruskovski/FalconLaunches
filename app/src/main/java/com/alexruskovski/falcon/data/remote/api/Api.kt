package com.alexruskovski.falcon.data.remote.api

import com.alexruskovski.falcon.Constants
import com.alexruskovski.falcon.model.Crew
import com.alexruskovski.falcon.model.Launch
import com.alexruskovski.falcon.model.Rocket
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Alexander Ruskovski on 28/06/2021
 */

interface Api {

    @GET(Constants.GET_LAUNCHES)
    suspend fun getAllLaunches(): Response<List<Launch>>

    @GET(Constants.GET_ROCKETS)
    suspend fun getRockets(): Response<List<Rocket>>

    @GET(Constants.GET_CREW)
    suspend fun getCrew(): Response<List<Crew>>

    @GET(Constants.GET_LAUNCH_DETAILS)
    suspend fun getSingleLaunch(
        @Path("id") launchId: String
    ): Response<Launch>


}