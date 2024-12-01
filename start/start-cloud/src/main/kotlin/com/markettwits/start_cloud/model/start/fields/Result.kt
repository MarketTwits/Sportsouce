package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable


@Serializable
data class Result(
    val createdAt: String,
    val file: File?,
    val fileID: Int?,
    val id: Int,
    val name: String,
    val start_id: Int,
    val updatedAt: String
)