package com.markettwits.cloud.model.image

import kotlinx.serialization.Serializable

@Serializable
data class UploadFileResponse(
    val fullPath: String,
    val id: Int,
)