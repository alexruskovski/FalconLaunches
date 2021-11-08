package com.alexruskovski.falcon.data.remote.api.usecases

import com.alexruskovski.falcon.data.remote.api.Api
import com.alexruskovski.falcon.data.remote.api.ApiUseCaseBase
import com.alexruskovski.falcon.data.remote.api.ApiResponseWrapper
import com.alexruskovski.falcon.model.Launch
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Alexander Ruskovski on 14/08/2021
 */

@Singleton
class UCGetSingleLaunchDetails @Inject constructor(
    private val api: Api
): ApiUseCaseBase<UCGetSingleLaunchDetails.Request, Launch>() {

    override suspend fun requestAsync(request: Request): ApiResponseWrapper<Launch> {
        val launchId = request.launchId
        return ApiResponseWrapper.create(api.getSingleLaunch(launchId))
    }

    @JsonClass(generateAdapter = true)
    data class Request(val launchId: String)

}