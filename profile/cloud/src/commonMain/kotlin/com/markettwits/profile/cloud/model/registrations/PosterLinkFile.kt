package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
data class PosterLinkFile(
    val createdAt: String,
    val extension: String,
    val fullPath: String,
    val id: Int,
    val lastModified: String?,
    val main_news_id: Int?,
    val name: String,
    val news_id: Int?,
    val path: String,
    val updatedAt: String
)