package com.alexruskovski.falcon.data.crew

import com.alexruskovski.falcon.data.Result
import com.alexruskovski.falcon.model.Crew


/**
 * Created by Alexander Ruskovski on 10/10/2021
 */

interface CrewRepository {

    /**
     * Returns list of Crew members wrapped in [Result]
     * */
    suspend fun getCrew(): Result<List<Crew>>

}