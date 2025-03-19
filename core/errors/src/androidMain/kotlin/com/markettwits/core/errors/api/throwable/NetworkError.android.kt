package com.markettwits.core.errors.api.throwable

import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

actual fun Throwable.isNetworkConnectionError(): Boolean {
    return when (this) {
        is ConnectException,
        is SocketException,
        is SocketTimeoutException,
        is TimeoutException,
        is UnknownHostException -> true
        else -> false
    }
}