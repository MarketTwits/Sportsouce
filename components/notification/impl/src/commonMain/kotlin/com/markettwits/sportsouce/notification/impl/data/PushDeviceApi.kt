package com.markettwits.sportsouce.notification.impl.data

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.notification.impl.domain.PushDeviceRepository
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class PushDeviceApi(provider: HttpClientProvider) : PushDeviceRepository {
    private val client = provider.provide(loggerEnabled = false)

    override suspend fun register(installationId: String, accessToken: String, fcmToken: String) {
        val response = client.put("notifications/push-devices/$installationId") {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject { put("fcmToken", fcmToken) })
        }
        check(response.status == HttpStatusCode.NoContent) { "Unexpected push registration response" }
    }

    override suspend fun unregister(installationId: String, accessToken: String) {
        val response = client.delete("notifications/push-devices/$installationId") {
            bearerAuth(accessToken)
        }
        check(response.status == HttpStatusCode.NoContent) { "Unexpected push unregistration response" }
    }
}
