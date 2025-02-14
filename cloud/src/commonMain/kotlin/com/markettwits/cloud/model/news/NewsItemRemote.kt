package com.markettwits.cloud.model.news

import com.markettwits.cloud.model.start.File
import kotlinx.serialization.Serializable

@Serializable
data class NewsItemRemote(
    val categoryId: Int,
    val createdAt: String,
    val full_description: String,
    val hashtags: List<HashtagRemote>,
    val id: Int,
    val images: List<Image>,
    val main_image : File?,
    val short_description: String,
    val title: String,
    val updatedAt: String,
    val viewsCount: Int
)