package com.alexruskovski.falcon.exceptions


/**
 * Created by Alexander Ruskovski on 06/11/2021
 */


abstract class ExceptionsBase(
    exception: Exception
): Exception(exception) {

    open fun userFriendlyMessage() = this.localizedMessage

}