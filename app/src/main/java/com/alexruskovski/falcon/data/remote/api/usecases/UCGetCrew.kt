package com.alexruskovski.falcon.data.remote.api.usecases

import com.alexruskovski.falcon.data.remote.api.Api
import com.alexruskovski.falcon.data.remote.api.ApiUseCaseBase
import com.alexruskovski.falcon.data.remote.api.ApiResponseWrapper
import com.alexruskovski.falcon.model.Crew
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Alexander Ruskovski on 10/10/2021
 *
 * Use Case that fetches the Falcon Crew
 */

@Singleton
class UCGetCrew @Inject constructor(
    private val api: Api
): ApiUseCaseBase<Unit, List<Crew>>() {

    override suspend fun requestAsync(request: Unit): ApiResponseWrapper<List<Crew>> {
        return ApiResponseWrapper.create(api.getCrew())
    }
}