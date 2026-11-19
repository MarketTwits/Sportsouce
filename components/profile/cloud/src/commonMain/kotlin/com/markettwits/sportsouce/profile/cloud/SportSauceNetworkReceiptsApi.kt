package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.cloud.model.receipts.PaidReceipt
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType

class SportSauceNetworkReceiptsApi(provider: HttpClientProvider) {
    // Receipts contain personal and payment details; keep their bodies out of HTTP logs.
    private val client = provider.provide(loggerEnabled = false)
    private val json = provider.json()

    suspend fun receipts(token: String): List<PaidReceipt> {
        val response = client.get("member-start/receipts/my") { bearerAuth(token) }
        return json.decodeFromString(response.bodyAsText())
    }

    suspend fun document(receiptId: Int, token: String): String {
        require(receiptId > 0)
        // Use the canonical API path, never a blob URL or a remote host from the list response.
        val response = client.get("member-start/receipts/$receiptId") {
            bearerAuth(token)
            accept(ContentType.Text.Html)
        }
        val html = response.bodyAsText()
        check(Regex("<(?:!doctype\\s+html|html|body|main|div)\\b", RegexOption.IGNORE_CASE).containsMatchIn(html)) {
            "Сервер не вернул документ квитанции"
        }
        return html
    }
}
