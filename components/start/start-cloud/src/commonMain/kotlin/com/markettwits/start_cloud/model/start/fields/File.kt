package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val createdAt: String,
    val extension: String,
    val fullPath: String,
    val id: Int,
    val main_news_id: Int?,
    val name: String,
    val path: String,
    val updatedAt: String
)