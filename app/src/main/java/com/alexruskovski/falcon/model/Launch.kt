package com.alexruskovski.falcon.model

import com.alexruskovski.falcon.Constants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.lang.Exception

/**
 * Created by Alexander Ruskovski on 28/06/2021
 *
 * Data-class that contains only the required data for the launch.
 */

@JsonClass(generateAdapter = true)
data class Launch(
    val id: String,
    val name: String,
    @Json(name = "date_utc") val launchDateUTC: String, //iso format
    val success: Boolean?,
    val links: Links,
    val details: String?
){
    fun getFormattedDate(): String{
        val format = DateTimeFormat.forPattern(Constants.LAUNCH_LIST_DATE_FORMAT)
        return try {
            format.print(DateTime(launchDateUTC))
        }catch (ex: Exception){
            "N/A"
        }
    }
}

@JsonClass(generateAdapter = true)
data class Links(
    val patch: Patch
)

@JsonClass(generateAdapter = true)
data class Patch(
    val small: String?,
    val large: String?
)
