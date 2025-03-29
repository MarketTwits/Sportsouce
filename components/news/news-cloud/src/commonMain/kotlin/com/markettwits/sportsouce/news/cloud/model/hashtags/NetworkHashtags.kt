package com.markettwits.sportsouce.news.cloud.model.hashtags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NetworkHashtags(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<NetworkHashtag>
)