package com.markettwits.sportsouce.club.cloud.models.workout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WorkoutRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<WorkoutRemoteRow>
)