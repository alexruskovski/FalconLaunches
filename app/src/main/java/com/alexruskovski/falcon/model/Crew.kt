package com.alexruskovski.falcon.model

import com.squareup.moshi.JsonClass


/**
 * Created by Alexander Ruskovski on 10/10/2021
 */
@JsonClass(generateAdapter = true)
data class Crew(
    val name: String,
    val agency: String,
    val image: String?,
    val wikipedia: String,
    val status: String
){
    val isActive = status == "active"
}
