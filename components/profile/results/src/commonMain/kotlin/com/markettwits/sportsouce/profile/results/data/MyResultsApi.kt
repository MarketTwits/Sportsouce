package com.markettwits.sportsouce.profile.results.data

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.results.domain.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal class MyResultsApi(provider: HttpClientProvider) {
    private val client = provider.provide(loggerEnabled = false)
    private val json = Json(provider.json()) { coerceInputValues = true }
    suspend fun series(token: String): List<ResultSeries> = json.decodeFromString(
        client.get("my-results/series") { bearerAuth(token) }.bodyAsText())
    suspend fun summary(token: String): ResultsSummary = json.decodeFromString(
        client.get("my-results/summary") { bearerAuth(token) }.bodyAsText())
    suspend fun feed(token: String, offset: Int, limit: Int, category: String?): ResultFeed = json.decodeFromString(
        client.get("my-results/feed") {
            bearerAuth(token); parameter("skipCount", offset); parameter("maxResultCount", limit); category?.let { parameter("category", it) }
        }.bodyAsText())
    suspend fun progress(token: String, category: String?, comparison: ResultComparison, series: String?): ResultProgress = json.decodeFromString(
        client.get("my-results/progress") {
            bearerAuth(token); category?.let { parameter("category", it) }; parameter("compare", comparison.apiValue); series?.let { parameter("series", it) }
        }.bodyAsText())
}
