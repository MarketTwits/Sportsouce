package com.markettwits.sportsouce.starts.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFile(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("extension")
    val extension: String,
    @SerialName("fullPath")
    val fullPath: String,
    @SerialName("id")
    val id: Int,
    @SerialName("lastModified")
    val lastModified: String?,
    @SerialName("main_news_id")
    val main_news_id: Int?,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: String,
    @SerialName("updatedAt")
    val updatedAt: String
)