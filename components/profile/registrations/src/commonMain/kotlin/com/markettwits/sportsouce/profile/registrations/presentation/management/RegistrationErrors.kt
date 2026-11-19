package com.markettwits.sportsouce.profile.registrations.presentation.management

import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_cloud.provider.ServiceResponseException
import com.markettwits.core_cloud.provider.responseErrorMessage
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import io.ktor.client.plugins.ResponseException

internal fun Throwable.registrationError(): String {
    val http = this as? ResponseException
    if (this is AuthException || http?.response?.status?.value == 401) {
        return "Сессия истекла. Войдите в аккаунт ещё раз."
    }

    val serverMessage = when (this) {
        is ServiceResponseException -> userMessage
        is ResponseException -> responseErrorMessage
        else -> null
    }
    val description = serverMessage.orEmpty().lowercase()
    return when {
        "already refunded" in description -> "Возврат по этому платежу уже оформлен."
        "payment is not refundable" in description -> "Возврат сейчас недоступен по правилам платёжной системы."
        "payment record not found" in description -> "Платёж не найден. Если ошибка повторится, обратитесь в поддержку."
        http != null && http.response.status.value in 500..599 &&
            serverMessage != null && serverMessage.any { it in 'А'..'я' || it == 'ё' || it == 'Ё' } -> serverMessage
        else -> mapToSauceError().mapToString()
    }
}
