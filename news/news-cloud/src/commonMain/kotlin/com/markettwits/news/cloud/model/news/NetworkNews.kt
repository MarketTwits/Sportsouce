package com.markettwits.news.cloud.model.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkNews(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<NetworkNewsItem>
)