package com.markettwits.cloud.model.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsToHashtag(
    val hashtag_id: Int,
    val id: Int,
    val news_id: Int
)