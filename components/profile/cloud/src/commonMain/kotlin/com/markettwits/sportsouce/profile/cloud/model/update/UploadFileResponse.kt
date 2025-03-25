package com.markettwits.sportsouce.profile.cloud.model.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileResponse(
    @SerialName("fullPath")
    val fullPath: String,
    @SerialName("id")
    val id: Int,
)