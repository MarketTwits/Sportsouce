package com.markettwits.cloud_clubs.models.workout

import com.markettwits.cloud_clubs.models.common.File
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRemoteRow(
    val createdAt: String,
    val description: String,
    val fileId: Int,
    val icon: File,
    val id: Int,
    val type: String,
    val updatedAt: String
)