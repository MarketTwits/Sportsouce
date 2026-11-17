package com.markettwits.core_cloud.provider

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.util.AttributeKey
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

private val ResponseErrorMessageKey = AttributeKey<String>("Sportsauce response error message")

val ResponseException.responseErrorMessage: String?
    get() = response.call.attributes.getOrNull(ResponseErrorMessageKey)

internal fun HttpClientConfig<*>.captureResponseErrorMessages(json: Json) {
    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            if (cause is ResponseException && cause.response.status.value in setOf(400, 401, 403, 409, 422)) {
                val message = try {
                    val body = json.parseToJsonElement(cause.response.bodyAsText()) as? JsonObject
                    when (val value = body?.get("message")) {
                        is JsonArray -> value.mapNotNull { (it as? JsonPrimitive)?.errorText() }.joinToString("\n")
                        is JsonPrimitive -> value.errorText()
                        else -> null
                    }
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    null
                }
                if (!message.isNullOrBlank()) cause.response.call.attributes.put(ResponseErrorMessageKey, message)
            }
        }
    }
}

private fun JsonPrimitive.errorText(): String? = content.trim().takeIf { isString && it.isNotEmpty() }
