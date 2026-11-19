package com.markettwits.core_cloud.provider

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.*

private val ResponseErrorMessageKey = AttributeKey<String>("Sportsauce response error message")

val ResponseException.responseErrorMessage: String?
    get() = response.call.attributes.getOrNull(ResponseErrorMessageKey)

/** A rejected operation returned inside a successful HTTP response. */
class ServiceResponseException(val userMessage: String) : IllegalStateException(userMessage)

internal fun HttpClientConfig<*>.captureResponseErrorMessages(json: Json) {
    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            if (cause is ResponseException && cause.response.status.value in 400..599) {
                val message = try {
                    val body = cause.response.bodyAsText()
                    val structured = runCatching { json.parseToJsonElement(body) }.getOrNull()
                    structured?.serviceErrorMessage() ?: body.trim().takeIf {
                        structured == null && cause.response.contentType()?.match(ContentType.Text.Plain) == true &&
                            it.isServiceMessage() && it.length <= 400 && '\n' !in it
                    }
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    null
                }
                if (message != null) cause.response.call.attributes.put(ResponseErrorMessageKey, message)
            }
        }
    }
}

/** Only explicitly designated error fields are eligible for display. */
fun JsonElement.serviceErrorMessage(): String? {
    val messages = when (this) {
        is JsonObject -> get("errors").errorTexts(fieldMap = true).ifEmpty {
            listOf("message", "detail", "error_description", "reason", "error", "title")
                .firstNotNullOfOrNull { key -> get(key).errorTexts().takeIf { it.isNotEmpty() } }.orEmpty()
        }
        else -> errorTexts()
    }
    return messages.distinct().take(8).joinToString("\n").takeIf(String::isNotBlank)
}

fun JsonElement.requireServiceSuccess(fallback: String) {
    if (this is JsonPrimitive && !isString && booleanOrNull == false) throw ServiceResponseException(fallback)
    val envelope = this as? JsonObject ?: return
    val status = ((envelope["statusCode"] ?: envelope["status"]) as? JsonPrimitive)?.intOrNull
    val rejected = listOf("success", "ok").any { (envelope[it] as? JsonPrimitive)?.booleanOrNull == false } ||
        (status != null && status in 400..599) || envelope["errors"].errorTexts(fieldMap = true).isNotEmpty() ||
        envelope["error"].errorTexts().isNotEmpty() || (envelope["error"] as? JsonPrimitive)?.let {
            if (it.isString) it.content.isNotBlank() else it.booleanOrNull == true
        } == true
    if (rejected) throw ServiceResponseException(envelope.serviceErrorMessage() ?: fallback)
}

private fun JsonElement?.errorTexts(fieldMap: Boolean = false, depth: Int = 0): List<String> {
    if (depth >= 6) return emptyList()
    return when (this) {
        is JsonPrimitive -> content.trim().takeIf { isString && it.isServiceMessage() }?.let(::listOf).orEmpty()
        is JsonArray -> take(8).flatMap { it.errorTexts(fieldMap, depth + 1) }
        is JsonObject -> listOf("message", "detail", "error_description", "reason", "constraints", "errors", "error", "title")
            .firstNotNullOfOrNull { key ->
                get(key).errorTexts(fieldMap = key == "constraints" || key == "errors", depth = depth + 1)
                    .takeIf { it.isNotEmpty() }
            } ?: if (fieldMap) values.take(8).flatMap { it.errorTexts(fieldMap = true, depth = depth + 1) } else emptyList()
        else -> emptyList()
    }
}

private val GenericHttpMessages = setOf("bad request", "unauthorized", "forbidden", "not found",
    "conflict", "unprocessable entity", "internal server error", "service unavailable")
private val FrameworkRouteError = Regex("^Cannot (GET|POST|PUT|PATCH|DELETE|HEAD|OPTIONS) /", RegexOption.IGNORE_CASE)

private fun String.isServiceMessage(): Boolean =
    isNotEmpty() && length <= 800 && '<' !in this && lowercase() !in GenericHttpMessages &&
        !FrameworkRouteError.containsMatchIn(this)
