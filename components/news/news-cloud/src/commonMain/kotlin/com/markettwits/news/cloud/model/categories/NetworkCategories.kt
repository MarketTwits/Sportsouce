package com.markettwits.news.cloud.model.categories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NetworkCategories(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<NetworkCategory>
)