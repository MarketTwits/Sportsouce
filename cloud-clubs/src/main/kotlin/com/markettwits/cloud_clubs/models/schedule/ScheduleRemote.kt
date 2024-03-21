package com.markettwits.cloud_clubs.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRemote(
    val count: Int,
    val rows: List<ScheduleRemoteRow>
)