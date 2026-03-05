package com.markettwits.sportsouce.news.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.news.cloud.model.categories.NetworkCategories
import com.markettwits.sportsouce.news.cloud.model.categories.NetworkCategory
import com.markettwits.sportsouce.news.cloud.model.hashtags.NetworkHashtag
import com.markettwits.sportsouce.news.cloud.model.hashtags.NetworkHashtags
import com.markettwits.sportsouce.news.cloud.model.news.NetworkNews
import com.markettwits.sportsouce.news.cloud.model.news.NetworkNewsItem
import io.ktor.client.call.*
import io.ktor.client.request.*

class SportSauceNewsNetworkApi(
    httpClient: HttpClientProvider,
) {
    private val json = httpClient.json()

    private val client = httpClient.provide()

    suspend fun news(newsId: String) = client.get("news/$newsId").body<NetworkNewsItem>()

    suspend fun news(
        categoryId: Int? = null,
        hashTag: String? = null,
        limit: Int = 10,
        offset: Int = 0,
        sorting: String = "createdAt DESC",
    ): NetworkNews {
        val response = client.get(
            if (categoryId != null) {
                "news/category/$categoryId"
            } else {
                "news"
            }
        ) {
            parameter("sorting", sorting)
            parameter("maxResultCount", limit)
            parameter("skipCount", if (offset == 0) "" else offset)
            parameter("hashTags", hashTag ?: "")
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
