package com.markettwits.sportsouce.profile.results.data

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.results.domain.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.*

internal class ResultsActionsApi(provider: HttpClientProvider) {
    private val client = provider.provide(loggerEnabled = false)
    private val json = Json(provider.json()) { coerceInputValues = true; explicitNulls = false }
    suspend fun starts(token: String, query: String): List<StartSuggestion> {
        return json.decodeFromString<StartSuggestions>(client.get("start/autocomplete") {
            bearerAuth(token); parameter("query", query); parameter("limit", 12)
        }.bodyAsText()).suggestions
    }
    suspend fun search(token: String, query: String, startId: Int?): List<UnmatchedResult> {
        return json.decodeFromString<UnmatchedResults>(client.get("identity/unmatched-results") {
            bearerAuth(token); query.trim().takeIf { it.isNotEmpty() }?.let { parameter("q", it) }; parameter("maxResultCount", 20)
            startId?.let { parameter("startId", it) }
        }.bodyAsText()).rows
    }
    suspend fun claims(token: String) = json.decodeFromString<ResultClaims>(get("identity/claims", token)).rows
    suspend fun candidates(token: String, dismissed: Boolean = false) = json.decodeFromString<ResultCandidates>(get(if (dismissed) "identity/candidates/dismissed" else "identity/candidates", token)).rows
    suspend fun suggestions(token: String) = json.decodeFromString<ResultClaims>(get("identity/suggestions", token)).rows
    private suspend fun get(path: String, token: String): String {
        return client.get(path) { bearerAuth(token) }.bodyAsText()
    }
    suspend fun claim(token: String, id: Int) = post(token, "identity/claims", buildJsonObject { put("resultId", id) })
    suspend fun candidate(token: String, id: Int, action: String) {
        require(action in setOf("accept", "dismiss", "restore"))
        post(token, "identity/candidates/$id/$action", buildJsonObject {})
    }
    suspend fun suggestion(token: String, id: Int, accept: Boolean) = post(token, "identity/suggestions/$id/respond", buildJsonObject { put("accept", accept) })
    suspend fun conflict(token: String, id: Int, transfer: Boolean) = post(token, "identity/conflicts/$id/respond", buildJsonObject { put("transfer", transfer) })
    suspend fun add(token: String, request: ExternalResultRequest) = post(token, "identity/missing-event-requests", json.encodeToJsonElement(request))
    private suspend fun post(token: String, path: String, body: JsonElement) {
        client.post(path) { bearerAuth(token); contentType(ContentType.Application.Json); setBody(body) }
    }
    suspend fun upload(token: String, file: ResultProofFile): Int {
        require(file.validationError() == null)
        val safeName = file.name.replace(Regex("[\\r\\n\\\"]"), "_")
        val response = client.submitFormWithBinaryData("file/upload", formData {
            append("file", file.bytes, Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"$safeName\"")
                append(HttpHeaders.ContentType, file.mimeType)
            })
            append("lastModified", file.lastModified.toString())
        }) { bearerAuth(token) }
        return json.parseToJsonElement(response.bodyAsText()).jsonObject.getValue("id").jsonPrimitive.int
    }
}
