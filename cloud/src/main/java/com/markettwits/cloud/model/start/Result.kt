package com.markettwits.cloud.model.start

import kotlinx.serialization.Serializable
import java.io.Serial

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