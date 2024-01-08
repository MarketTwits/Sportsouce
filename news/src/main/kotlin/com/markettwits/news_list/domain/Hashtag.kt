package com.markettwits.news_list.domain

import kotlinx.serialization.Serializable

@Serializable
data class Hashtag(
    val id: Int,
    val name: String
)