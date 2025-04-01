package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Result(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("file")
    val file: File?,
    @SerialName("fileID")
    val fileID: Int?,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("updatedAt")
    val updatedAt: String
)