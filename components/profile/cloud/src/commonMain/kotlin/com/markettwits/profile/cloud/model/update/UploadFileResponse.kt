package com.markettwits.profile.cloud.model.update

import kotlinx.serialization.Serializable

@Serializable
data class UploadFileResponse(
    val fullPath: String,
    val id: Int,
)