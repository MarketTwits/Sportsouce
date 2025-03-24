package com.markettwits.news.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.news.cloud.model.categories.NetworkCategories
import com.markettwits.news.cloud.model.categories.NetworkCategory
import com.markettwits.news.cloud.model.hashtags.NetworkHashtag
import com.markettwits.news.cloud.model.hashtags.NetworkHashtags
import com.markettwits.news.cloud.model.news.NetworkNews
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SportSauceNewsNetworkApi(
    httpClient: HttpClientProvider,
    isLoggerEnabled: Boolean = true,
) {
    private val json = httpClient.json()

    private val client = httpClient.provide(isLoggerEnabled)

    suspend fun news(limit: Int = 10, offset: Int = 0): NetworkNews {
        val response = client.get("news") {
            parameter("sorting", "createdAt DESC")
            parameter("maxResultCount", limit)
            parameter("skipCount", offset)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun hashtags(limit: Int = 10, offset: Int = 0): List<NetworkHashtag> {
        val response = client.get("hash-tag") {
            parameter("sorting", "createdAt DESC")
            parameter("maxResultCount", limit)
            parameter("skipCount", offset)
        }
        return json.decodeFromString<NetworkHashtags>(response.body()).rows
    }

    suspend fun categories(limit: Int = 10, offset: Int = 0): List<NetworkCategory> {
        val response = client.get("news-category") {
            parameter("sorting", "createdAt DESC")
            parameter("maxResultCount", limit)
            parameter("skipCount", offset)
        }
        return json.decodeFromString<NetworkCategories>(response.body()).rows
    }

}