package com.markettwits.sportsouce.news.common.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsItem(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val imageUrl: String,
    val createData: String,
    val hashtags: List<NewsHashtag>
)