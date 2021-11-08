package com.alexruskovski.falcon

object Constants {

    /** LAUNCH LIST */
    const val LAUNCH_LIST_DATE_FORMAT = "dd-MM-yyyy"

    /** API */
    private const val API_VERSION = "v4"
    const val API_ENDPOINT = "https://api.spacexdata.com/$API_VERSION/"

    const val GET_LAUNCHES = "launches/"
    const val GET_LAUNCH_DETAILS = "launches/{id}"

    const val GET_ROCKETS = "rockets"

    const val GET_CREW = "crew"

}