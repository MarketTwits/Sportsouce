package com.markettwits.club.cloud.exception

import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.isNetworkConnectionError(): Boolean {
    return when (this) {
        is ConnectException,
        is SocketException,
        is io.ktor.client.network.sockets.SocketTimeoutException,
        is TimeoutException,
        is UnknownHostException -> true

        else -> false
    }
}