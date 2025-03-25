package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PosterLinkFile(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("extension")
    val extension: String,
    @SerialName("fullPath")
    val fullPath: String,
    @SerialName("id")
    val id: Int,
    @SerialName("lastModified")
    val lastModified: String?,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: String,
    @SerialName("updatedAt")
    val updatedAt: String
)