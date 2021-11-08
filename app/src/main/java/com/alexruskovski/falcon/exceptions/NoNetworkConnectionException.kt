package com.alexruskovski.falcon.exceptions

import java.lang.Exception


/**
 * Created by Alexander Ruskovski on 06/11/2021
 */


class NoNetworkConnectionException constructor(
    private val customMessage: String = "Ups, looks like you don't have internet access. Please check your connection and try again!",
    exception: Exception = Exception(customMessage)
) : ExceptionsBase(exception) {

    override fun userFriendlyMessage(): String {
        return customMessage
    }

}