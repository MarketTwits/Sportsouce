package com.markettwits.sportsouce.news.common.model

import kotlinx.serialization.Serializable


@Serializable
data class NewsCategory(
    val id: Int,
    val name: String
)