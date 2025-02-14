package com.markettwits.cloud.model.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsRemote(
    val count: Int,
    val rows: List<NewsItemRemote>
)