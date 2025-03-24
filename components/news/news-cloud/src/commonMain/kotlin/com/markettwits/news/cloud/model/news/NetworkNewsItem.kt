package com.markettwits.news.cloud.model.news

import com.markettwits.news.cloud.model.hashtags.NetworkHashtag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkNewsItem(
    @SerialName("categoryId")
    val categoryId: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("full_description")
    val fullDescription: String,
    @SerialName("hashtags")
    val hashtags: List<NetworkHashtag>,
    @SerialName("id")
    val id: Int,
    @SerialName("images")
    val images: List<NetworkImage>,
    @SerialName("main_image")
    val mainImage: NetworkImage?,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("title")
    val title: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("viewsCount")
    val viewsCount: Int
)