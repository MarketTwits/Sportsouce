package com.markettwits.news_list.domain

import kotlinx.serialization.Serializable

@Serializable
data class NewsInfo(
    val id: Int,
    val title: String,
    @SuppressWarnings("html_property")
    val shortDescription: String,
    @SuppressWarnings("html_property")
    val fullDescription: String,
    val imageUrl: String,
    val createData: String,
    val hashtags: List<Hashtag>
)