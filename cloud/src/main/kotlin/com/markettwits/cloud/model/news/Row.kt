package com.markettwits.cloud.model.news

import kotlinx.serialization.Serializable

@Serializable
data class Row(
    val categoryId: Int,
    val createdAt: String,
    val full_description: String,
    val hashtags: List<Hashtag>,
    val id: Int,
    val images: List<Image>,
    val short_description: String,
    val title: String,
    val updatedAt: String,
    val viewsCount: Int
)