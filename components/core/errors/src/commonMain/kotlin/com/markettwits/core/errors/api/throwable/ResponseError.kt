package com.markettwits.core.errors.api.throwable

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@Serializable
data class ResponseError(
    val message: String,
    val path: String,
    val statusCode: Int,
    val timestamp: String
)

fun Throwable.isResponseException(): Boolean =
    (this.message?.contains("error", ignoreCase = true) == true ||
            this.message?.contains("response", ignoreCase = true) == true ||
            this is SerializationException)