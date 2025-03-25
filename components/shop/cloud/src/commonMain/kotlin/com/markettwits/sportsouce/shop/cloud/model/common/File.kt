package com.markettwits.sportsouce.shop.cloud.model.common

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val createdAt: String? = null,
    val extension: String? = null,
    val fullPath: String,
    val id: Int,
    val lastModified: String? = null,
    val name: String,
    val path: String,
    val updatedAt: String? = null
)