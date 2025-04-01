package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionFile(
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