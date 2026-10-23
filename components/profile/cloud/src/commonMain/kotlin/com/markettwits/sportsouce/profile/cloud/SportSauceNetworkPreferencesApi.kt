package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.cloud.model.preferences.ProfileCompleteness
import com.markettwits.sportsouce.profile.cloud.model.preferences.UpdateProfilePreferencesRequest
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class SportSauceNetworkPreferencesApi(provider: HttpClientProvider) {
    private val client = provider.provide(loggerEnabled = false)
    private val json = Json(provider.json()) { coerceInputValues = true }

    suspend fun completeness(token: String): ProfileCompleteness = json.decodeFromString(
        client.get("identity/me/completeness") { bearerAuth(token) }.bodyAsText()
    )

    suspend fun update(request: UpdateProfilePreferencesRequest, token: String): ProfileCompleteness = json.decodeFromString(
        client.patch("identity/me/preferences") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyAsText()
    )
}
