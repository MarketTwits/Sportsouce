package com.markettwits.cloud_clubs.models.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRemote(
    val count: Int,
    val rows: List<WorkoutRemoteRow>
)