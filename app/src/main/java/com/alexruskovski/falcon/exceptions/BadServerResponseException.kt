package com.alexruskovski.falcon.exceptions


/**
 * Created by Alexander Ruskovski on 06/11/2021
 */


class BadServerResponseException constructor(
    private val customMessage: String ?= null,
    exception: Exception = Exception(customMessage)
) : ExceptionsBase(exception) {

    constructor(exception: Exception): this(null, exception)

    override fun userFriendlyMessage(): String = customMessage ?: this.localizedMessage ?: "Ups, bad server response. Please try again."

}