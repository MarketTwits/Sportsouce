package com.markettwits.core.errors.api.throwable

import com.markettwits.core.log.TaggedLogger
import com.markettwits.core.log.errorLog
import com.markettwits.core_cloud.provider.ServiceResponseException
import com.markettwits.core_cloud.provider.responseErrorMessage
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

fun Throwable.mapToSauceError(): SauceError {
    if (this is CancellationException) throw this
    if (this is SauceError) return this

    TaggedLogger("SauceError").errorLog(this) { "Failed to execute operation" }
    return when {
        this is ServiceResponseException -> SauceError.WrongRequest(this)
        this is ResponseException -> mapResponseError()
        this is HttpRequestTimeoutException || this.isNetworkConnectionError() -> SauceError.Connection(this)
        this is SerializationException || this is ContentConvertException || this is NoTransformationFoundException ->
            SauceError.JsonConverter(this)
        else -> SauceError.General(this)
    }
}

private fun ResponseException.mapResponseError(): SauceError =
    when (response.status.value) {
        404, 410 -> responseErrorMessage?.let { SauceError.WrongRequest(Exception(it, this)) } ?: SauceError.NotFound(this)
        408 -> SauceError.Connection(this)
        429 -> SauceError.General(this, "Слишком много запросов. Попробуйте немного позже.")
        in 500..599 -> SauceError.General(this, "Сервис временно недоступен. Попробуйте позже.")
        in 400..499 -> {
            val message = responseErrorMessage ?: when (response.status.value) {
                401 -> "Для продолжения войдите в аккаунт."
                403 -> "У вас нет доступа к этим данным."
                else -> "Не удалось выполнить запрос. Проверьте введённые данные."
            }
            SauceError.WrongRequest(Exception(message, this))
        }
        else -> SauceError.General(this)
    }

fun SauceError.mapToString(): String = when (this) {
    is SauceError.Connection -> "Не удалось подключиться. Проверьте интернет и попробуйте снова."
    is SauceError.Empty -> "Ничего не найдено."
    is SauceError.General -> userMessage ?: "Произошла ошибка. Попробуйте ещё раз."
    is SauceError.JsonConverter -> "Не удалось обработать данные. Попробуйте позже."
    is SauceError.NotFound -> "Запрошенные данные не найдены."
    is SauceError.WrongRequest -> exception.message?.takeIf { it.isNotBlank() } ?: "Не удалось выполнить запрос."
}
