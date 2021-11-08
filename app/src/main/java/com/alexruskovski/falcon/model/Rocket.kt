package com.alexruskovski.falcon.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by Alexander Ruskovski on 09/10/2021
 */

@JsonClass(generateAdapter = true)
data class Rocket(
    val name: String = "N/A",
    val type: String?,
    val active: Boolean = false,
    @Json(name = "cost_per_launch")
    val costPerLaunch: Float?,
    @Json(name = "first_flight")
    val firstFlight: String?, //ref 2006-03-24
    val country: String?,
    val wikipedia: String?,
    val description: String?,
    @Json(name = "flickr_images")
    val flickrImages: List<String>?

){
    @Json(name = "height")
    data class Height(
        val meter: Float
    )

    @Json(name = "diameter")
    data class Diameter(
        val meter: Float
    )

    @Json(name = "mass")
    data class Mass(
        val kg: Float
    )

}

