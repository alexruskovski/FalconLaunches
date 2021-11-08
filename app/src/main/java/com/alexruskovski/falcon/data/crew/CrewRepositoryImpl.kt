package com.alexruskovski.falcon.data.crew

import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.data.remote.api.ApiEmptyResponse
import com.alexruskovski.falcon.data.remote.api.ApiErrorResponse
import com.alexruskovski.falcon.data.remote.api.ApiSuccessResponse
import com.alexruskovski.falcon.data.remote.api.usecases.UCGetCrew
import com.alexruskovski.falcon.exceptions.BadServerResponseException
import com.alexruskovski.falcon.model.Crew


/**
 * Created by Alexander Ruskovski on 10/10/2021
 */

class CrewRepositoryImpl constructor(
    private val ucGetCrew: UCGetCrew
): CrewRepository {

    override suspend fun getCrew(): Result<List<Crew>> {
        val response = ucGetCrew.requestAsync(Unit)
        return try{
            when(response){
                is ApiEmptyResponse -> Result.EmptyResponse
                is ApiErrorResponse -> Result.Error(BadServerResponseException())
                is ApiSuccessResponse -> Result.Success(data = response.body)
            }
        }catch (ex: Exception){
            Result.Error(BadServerResponseException(exception = ex))
        }
    }
}