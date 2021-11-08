package com.alexruskovski.falcon.data.rockets

import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.model.Rocket


/**
 * Created by Alexander Ruskovski on 09/10/2021
 */


interface  RocketsRepository {

    /**
     * Get all Rockets from the API
     * */
    suspend fun getAllRockets(): Result<List<Rocket>>

}