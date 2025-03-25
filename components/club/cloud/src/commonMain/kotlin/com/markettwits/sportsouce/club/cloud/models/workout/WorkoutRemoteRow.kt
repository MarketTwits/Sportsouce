package com.markettwits.sportsouce.club.cloud.models.workout

import com.markettwits.sportsouce.club.cloud.models.common.File
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRemoteRow(
    @SerialName("description")
    val description: String,
    @SerialName("fileId")
    val fileId: Int?,
    @SerialName("icon")
    val icon: File?,
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String,
)