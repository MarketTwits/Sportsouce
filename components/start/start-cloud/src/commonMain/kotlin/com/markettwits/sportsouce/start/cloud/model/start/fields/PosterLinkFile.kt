package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class PosterLinkFile(
    val fullPath: String,
    val id: Int,
    val name: String,
    val path: String,
)