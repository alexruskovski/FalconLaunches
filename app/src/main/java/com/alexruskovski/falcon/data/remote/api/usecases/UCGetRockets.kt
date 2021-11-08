package com.alexruskovski.falcon.data.remote.api.usecases

import com.alexruskovski.falcon.data.remote.api.Api
import com.alexruskovski.falcon.data.remote.api.ApiUseCaseBase
import com.alexruskovski.falcon.data.remote.api.ApiResponseWrapper
import com.alexruskovski.falcon.model.Rocket
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Alexander Ruskovski on 09/10/2021
 */

@Singleton
class UCGetRockets @Inject constructor(
    private val api: Api
): ApiUseCaseBase<Unit, List<Rocket>>(){

    override suspend fun requestAsync(request: Unit): ApiResponseWrapper<List<Rocket>> {
        return ApiResponseWrapper.create(api.getRockets())
    }
}