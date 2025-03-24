package com.markettwits.core.errors.api.throwable

import io.ktor.client.network.sockets.SocketTimeoutException

actual fun Throwable.isNetworkConnectionError(): Boolean {
    return when (this) {
        is SocketTimeoutException -> true
        else -> isJsNetworkError()
    }
}

private fun Throwable.isJsNetworkError(): Boolean {
    val message = this.message?.lowercase() ?: return false
    return message.contains("network") ||
            message.contains("fetch failed") ||
            message.contains("failed to fetch") ||
            message.contains("offline")
}