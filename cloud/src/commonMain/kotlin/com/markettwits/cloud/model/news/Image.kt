package com.markettwits.cloud.model.news

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val extension: String,
    val fullPath: String,
    val id: Int,
    val name: String,
    val path: String,
)