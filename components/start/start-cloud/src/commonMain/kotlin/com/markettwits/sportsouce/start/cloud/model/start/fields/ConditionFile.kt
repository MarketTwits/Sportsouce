package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class ConditionFile(
    val createdAt: String,
    val extension: String,
    val fullPath: String,
    val id: Int,
    val lastModified: String?,
    val main_news_id: String?,
    val name: String,
    val news_id: Int?,
    val path: String,
    val updatedAt: String
)