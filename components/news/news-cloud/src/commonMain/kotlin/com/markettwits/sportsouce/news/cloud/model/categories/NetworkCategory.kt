package com.markettwits.sportsouce.news.cloud.model.categories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)