package com.markettwits.cloud.exception

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException


suspend fun networkExceptionHandler(exception: Throwable): Exception =
    when (exception) {
        is AuthException -> Exception(exception.message)
        is java.net.UnknownHostException -> Exception("Проблема интернет соединения")
        is HttpRequestTimeoutException -> Exception("Неустойчивое интернет соединение")
        is SocketTimeoutException -> Exception("Ошибка обработки запроса")
        is ResponseException -> Exception(exception.response.body<AuthErrorResponse>().message)
        is SerializationException -> Exception("Ошибка обработки данных")
        else -> Exception(exception.message.toString())
    }

fun Throwable.isResponseException(): Boolean =
    (this is ResponseException || this is SerializationException)

