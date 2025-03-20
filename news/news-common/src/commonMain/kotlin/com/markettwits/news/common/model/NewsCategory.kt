package com.markettwits.news.common.model

import kotlinx.serialization.Serializable


@Serializable
data class NewsCategory(
    val id: Int,
    val name: String
)