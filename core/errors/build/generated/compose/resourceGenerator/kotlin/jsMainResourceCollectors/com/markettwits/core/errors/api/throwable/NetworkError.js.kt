package com.markettwits.core.errors.api.throwable

actual fun Throwable.isNetworkConnectionError(): Boolean {
    return when (this) {
        is Error -> {
            this.message?.contains("NetworkError") == true ||
                    this.message?.contains("Failed to fetch") == true ||
                    this.message?.contains("timeout") == true ||
                    this.message?.contains("ETIMEDOUT") == true ||
                    this.message?.contains("ENOTFOUND") == true
        }
        else -> false
    }
}