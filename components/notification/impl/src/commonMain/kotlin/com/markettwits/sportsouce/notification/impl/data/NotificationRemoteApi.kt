package com.markettwits.sportsouce.notification.impl.data

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.api.model.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class NotificationRemoteApi(provider: HttpClientProvider) {
    private val client = provider.provide(loggerEnabled = false)
    private val json = Json(provider.json()) { coerceInputValues = true }

    suspend fun page(token: String, offset: Int, limit: Int): NotificationPage = json.decodeFromString(
        client.get("notifications") {
            bearerAuth(token)
            parameter("skipCount", offset)
            parameter("maxResultCount", limit)
            parameter("sorting", "createdAt DESC")
        }.bodyAsText()
    )

    suspend fun unreadCount(token: String): Int = json.decodeFromString<Count>(
        client.get("notifications/unread-count") { bearerAuth(token) }.bodyAsText()
    ).count.coerceAtLeast(0)

    suspend fun read(token: String, id: Int): InboxNotification = json.decodeFromString(
        client.patch("notifications/$id/read") { bearerAuth(token) }.bodyAsText()
    )

    suspend fun readAll(token: String) {
        client.patch("notifications/read-all") { bearerAuth(token) }
    }

    @Serializable private data class Count(val count: Int = 0)
}
