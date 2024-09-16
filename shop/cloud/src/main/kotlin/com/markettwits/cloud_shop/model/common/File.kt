package com.markettwits.cloud_shop.model.common

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val createdAt: String,
    val extension: String,
    val fullPath: String,
    val id: Int,
    val lastModified: String,
    val name: String,
    val path: String,
    val updatedAt: String
)