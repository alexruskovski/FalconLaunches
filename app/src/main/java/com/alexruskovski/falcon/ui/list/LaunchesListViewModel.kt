package com.alexruskovski.falcon.ui.list

import androidx.lifecycle.*
import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.data.crew.CrewRepository
import com.alexruskovski.falcon.data.launches.LaunchesRepository
import com.alexruskovski.falcon.data.rockets.RocketsRepository
import com.alexruskovski.falcon.model.Launch
import com.alexruskovski.falcon.model.Rocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Alexander Ruskovski on 15/08/2021
 */

@HiltViewModel
class LaunchesListViewModel @Inject constructor(
    private val launchesRepository: LaunchesRepository,
    private val rocketsRepository: RocketsRepository,
    private val crewRepository: CrewRepository
) : ViewModel() {

    suspend fun getAllLaunches(): Result<List<Launch>> {
        return launchesRepository.getAllLaunches()
    }

    fun getAllRockets() = flow<Result<List<Rocket>>> {
        emit(rocketsRepository.getAllRockets())
    }

    fun getCrew() = flow {
        emit(crewRepository.getCrew())
    }

}