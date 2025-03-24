package com.markettwits.news.cloud.model.hashtags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHashtag(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)