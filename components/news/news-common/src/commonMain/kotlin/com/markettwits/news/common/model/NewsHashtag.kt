package com.markettwits.news.common.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsHashtag(
    val id: Int,
    val name: String
)