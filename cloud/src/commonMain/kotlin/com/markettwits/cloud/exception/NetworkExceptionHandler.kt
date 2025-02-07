package com.markettwits.cloud.exception

import com.markettwits.cloud.model.auth.common.ErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException


suspend fun networkExceptionHandler(exception: Throwable): Exception = when (exception) {
        is AuthException -> Exception(exception.message)
       // is UnknownHostException -> Exception("Проблема интернет соединения")
        is HttpRequestTimeoutException -> Exception("Неустойчивое интернет соединение")
        is SocketTimeoutException -> Exception("Ошибка обработки запроса")
        is ResponseException -> Exception(exception.response.body<ErrorResponse>().message)
        is SerializationException -> Exception("Ошибка обработки данных")
        else -> Exception(exception.message.toString())
    }

fun Throwable.isNetworkConnectionError(): Boolean {
   // return when (this) {
//        is ConnectException,
//        is SocketException,
    //    is SocketTimeoutException,
//        is TimeoutException,
//        is UnknownHostException -> true
    return false
}
fun Throwable.isResponseException(): Boolean =
    (this is ResponseException || this is SerializationException)

