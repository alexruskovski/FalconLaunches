package com.alexruskovski.falcon.data.remote.api

import retrofit2.Response

/**
 * Created by Alexander Ruskovski on 14/08/2021
 * Just a wrapper for the for the Retrofit's Reponse
 */


sealed class ApiResponseWrapper<T> {

    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponseWrapper<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body = body
                    )
                }
            } else {
                val errBody = response.errorBody()
                val isHtml = errBody?.contentType().toString().contains("html")
                if (isHtml) {
                    val htmlErrMessage =
                        "Ups, unexpected server response. (html returned). Please try again shortly."
                    return ApiErrorResponse(htmlErrMessage)
                }
                val msg = errBody?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponseWrapper<T>()

data class ApiSuccessResponse<T>(
    val body: T
) : ApiResponseWrapper<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponseWrapper<T>()
