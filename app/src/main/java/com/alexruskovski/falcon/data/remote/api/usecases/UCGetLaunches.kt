package com.alexruskovski.falcon.data.remote.api.usecases

import com.alexruskovski.falcon.data.remote.api.Api
import com.alexruskovski.falcon.data.remote.api.ApiUseCaseBase
import com.alexruskovski.falcon.data.remote.api.ApiResponseWrapper
import com.alexruskovski.falcon.model.Launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Alexander Ruskovski on 14/08/2021
 */

@Singleton
class UCGetLaunches @Inject constructor(
    private val api: Api
): ApiUseCaseBase<Unit, List<Launch>>() {

    override suspend fun requestAsync(request: Unit): ApiResponseWrapper<List<Launch>> {
        return ApiResponseWrapper.create(api.getAllLaunches())
    }

}