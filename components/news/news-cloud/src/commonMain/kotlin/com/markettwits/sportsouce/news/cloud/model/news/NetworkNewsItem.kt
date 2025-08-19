package com.markettwits.sportsouce.news.cloud.model.news

import com.markettwits.sportsouce.news.cloud.model.hashtags.NetworkHashtag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkNewsItem(
    @SerialName("id")
    val id: Int,
    @SerialName("categoryId")
    val categoryId: Int? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("full_description")
    val fullDescription: String? = null,
    @SerialName("hashtags")
    val hashtags: List<NetworkHashtag>? = null,
    @SerialName("images")
    val images: List<NetworkImage>? = null,
    @SerialName("main_image")
    val mainImage: NetworkImage? = null,
    @SerialName("short_description")
    val shortDescription: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("viewsCount")
    val viewsCount: Int? = null
)