package com.alexruskovski.falcon.data.remote.api

import retrofit2.Response
import java.lang.Exception

/**
 * Created by Alexander Ruskovski on 14/08/2021
 *
 * ApiBase class that is meant to be extended from all API use cases.
 * T - Request class
 * R - Response that will be casted to before returning
 */

abstract class ApiUseCaseBase<T,R> {

    protected fun request(res: Response<R>): ApiResponseWrapper<R> {
        return try {
            ApiResponseWrapper.create(res)
        }catch (ex: Exception){
            return ApiResponseWrapper.create(ex)
        }
    }

    abstract suspend fun requestAsync(request:T): ApiResponseWrapper<R>

}