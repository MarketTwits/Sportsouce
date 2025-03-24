package com.markettwits.core.errors.api.throwable

import io.ktor.client.plugins.ResponseException
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
    (this is ResponseException || this is SerializationException)