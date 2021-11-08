package com.alexruskovski.falcon.data.rockets

import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.data.remote.api.ApiEmptyResponse
import com.alexruskovski.falcon.data.remote.api.ApiErrorResponse
import com.alexruskovski.falcon.data.remote.api.ApiSuccessResponse
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetRockets
import com.alexruskovski.falcon.exceptions.BadServerResponseException
import com.alexruskovski.falcon.model.Rocket


/**
 * Created by Alexander Ruskovski on 09/10/2021
 */


class RocketsRepositoryImpl constructor(
    private val ucGetRockets: UCGetRockets
): RocketsRepository {

    override suspend fun getAllRockets(): Result<List<Rocket>> {
        val response = ucGetRockets.requestAsync(Unit)
        return try{
            when(response){
                is ApiEmptyResponse -> Result.EmptyResponse
                is ApiErrorResponse -> Result.Error(BadServerResponseException())
                is ApiSuccessResponse -> {
                    Result.Success(data = response.body)
                }
            }
        }catch (ex: Exception){
            Result.Error(BadServerResponseException(exception = ex))
        }
    }


}