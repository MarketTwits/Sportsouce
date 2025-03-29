package com.markettwits.sportsouce.news.common.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsHashtag(
    val id: Int,
    val name: String
)