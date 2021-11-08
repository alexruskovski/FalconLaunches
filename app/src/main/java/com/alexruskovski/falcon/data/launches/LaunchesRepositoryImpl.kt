package com.alexruskovski.falcon.data.launches

import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.data.remote.api.ApiEmptyResponse
import com.alexruskovski.falcon.data.remote.api.ApiErrorResponse
import com.alexruskovski.falcon.data.remote.api.ApiSuccessResponse
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetLaunches
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetSingleLaunchDetails
import com.alexruskovski.falcon.exceptions.BadServerResponseException
import com.alexruskovski.falcon.exceptions.NoNetworkConnectionException
import com.alexruskovski.falcon.model.Launch
import java.net.UnknownHostException

/**
 * Created by Alexander Ruskovski on 14/08/2021
 */

class LaunchesRepositoryImpl(
    private val ucGetLaunches: UCGetLaunches,
    private val ucGetSingleLaunchDetails: UCGetSingleLaunchDetails
) : LaunchesRepository {


    override suspend fun getAllLaunches(): Result<List<Launch>> {
        return try{
            when(val response = ucGetLaunches.requestAsync(Unit)){
                is ApiEmptyResponse -> Result.EmptyResponse
                is ApiErrorResponse -> Result.Error(BadServerResponseException())
                is ApiSuccessResponse -> {
                    Result.Success(data = response.body)
                }
            }
        }catch (ex: UnknownHostException){
            Result.Error(NoNetworkConnectionException())//handles no internet connection exceptions
        } catch (ex: Exception){
            Result.Error(BadServerResponseException(ex))
        }
    }

    override suspend fun getSingleLaunchDetails(launchId: String): Result<Launch> {
        return try {
            val request = UCGetSingleLaunchDetails.Request(launchId)
            when(val response = ucGetSingleLaunchDetails.requestAsync(request)){
                is ApiEmptyResponse -> Result.EmptyResponse
                is ApiErrorResponse -> Result.Error(BadServerResponseException())
                is ApiSuccessResponse -> {
                    Result.Success(data = response.body)
                }
            }
        }catch (ex: UnknownHostException){
            Result.Error(NoNetworkConnectionException())
        }catch (ex: Exception){
            Result.Error(BadServerResponseException(exception = ex))
        }
    }

}