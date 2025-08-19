package com.markettwits.sportsouce.news.cloud.model.hashtags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHashtag(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
)