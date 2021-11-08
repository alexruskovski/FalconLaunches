package com.alexruskovski.falcon.ui.details

import androidx.lifecycle.ViewModel
import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.data.launches.LaunchesRepository
import com.alexruskovski.falcon.model.Launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * Created by Alexander Ruskovski on 26/08/2021
 */
@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val launchesRepository: LaunchesRepository
) : ViewModel() {

    suspend fun getSingleLaunchDetails(
        launchId: String
    ): Result<Launch> {
        return when (
            val response = launchesRepository.getSingleLaunchDetails(launchId)
        ) {
            is Result.Success -> response
            is Result.Error -> Result.Error(response.exception)
            is Result.EmptyResponse -> Result.EmptyResponse
        }
    }

}