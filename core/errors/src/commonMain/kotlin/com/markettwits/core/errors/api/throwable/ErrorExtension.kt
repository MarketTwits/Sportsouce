package com.markettwits.core.errors.api.throwable

import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException
import sportsouce.core.errors.generated.resources.Res
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

suspend fun Throwable.mapToSauceError(): SauceError = when (this) {
    is UnknownHostException -> SauceError.Connection(this)
    is HttpRequestTimeoutException -> SauceError.Connection(this)
    is SocketTimeoutException -> SauceError.Connection(this)
    is ResponseException -> SauceError.WrongRequest(Exception(this.response.body<ResponseError>().message))
    is SerializationException -> SauceError.JsonConverter(this)
    else -> SauceError.General(this)
}

fun SauceError.mapToString() : String = when(this){
    is SauceError.Connection -> NETWORK_EXCEPTION_MESSAGE
    is SauceError.Empty -> EMPTY_EXCEPTION_MESSAGE
    is SauceError.General -> GENERAL_EXCEPTION_MESSAGE
    is SauceError.JsonConverter -> SERIALIZATION_EXCEPTION_MESSAGE
    is SauceError.NotFound -> EMPTY_EXCEPTION_MESSAGE
    is SauceError.WrongRequest -> this.message.toString()
}

suspend fun Throwable.networkExceptionHandler(): Exception = when (this) {
    is UnknownHostException -> Exception(NETWORK_EXCEPTION_MESSAGE)
    is HttpRequestTimeoutException -> Exception(NETWORK_EXCEPTION_MESSAGE)
    is SocketTimeoutException -> Exception(TIMEOUT_EXCEPTION_MESSAGE)
    is ResponseException -> Exception(this.response.body<ResponseError>().message)
    is SerializationException -> Exception(SERIALIZATION_EXCEPTION_MESSAGE)
    else -> Exception(this.message.toString())
}

private const val NETWORK_EXCEPTION_MESSAGE = "Проблема интернет с  соединением"
private const val TIMEOUT_EXCEPTION_MESSAGE = "Не удалось загрузить данные"
private const val SERIALIZATION_EXCEPTION_MESSAGE = "Ошибка обработки полученных данных"
private const val GENERAL_EXCEPTION_MESSAGE = "Что то пошло не так..."
private const val EMPTY_EXCEPTION_MESSAGE = "Ничего не найдено"


