package com.markettwits.core.errors.api.throwable


fun Throwable.isNetworkConnectionError(): Boolean {
    return when (this) {
//        is ConnectException,
//        is SocketException,
//        is SocketTimeoutException,
//        is TimeoutException,
//        is UnknownHostException -> true
        else -> false
    }
}