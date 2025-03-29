package com.markettwits.sportsouce.news.cloud.model.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    @SerialName("extension")
    val extension: String,
    @SerialName("fullPath")
    val fullPath: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: String,
)