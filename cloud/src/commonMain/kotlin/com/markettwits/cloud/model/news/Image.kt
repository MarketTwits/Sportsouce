package com.markettwits.cloud.model.news

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val createdAt: String,
    val extension: String,
    val fullPath: String,
    val id: Int,
    val lastModified: String,
    val name: String,
    val news_id: Int,
    val path: String,
    val updatedAt: String
)