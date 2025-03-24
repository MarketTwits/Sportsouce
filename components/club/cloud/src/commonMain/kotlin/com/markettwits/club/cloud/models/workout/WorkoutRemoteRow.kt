package com.markettwits.club.cloud.models.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRemoteRow(
    val description: String,
    val fileId: Int?,
    val icon: com.markettwits.club.cloud.models.common.File?,
    val id: Int,
    val type: String,
)