package com.markettwits.club.cloud.models.workout

import kotlinx.serialization.Serializable

@Serializable
internal data class WorkoutRemote(
    val count: Int,
    val rows: List<WorkoutRemoteRow>
)