package com.markettwits.news.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkNewsToHashtag(
    @SerialName("hashtag_id")
    val hashtagId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("news_id")
    val newsId: Int
)