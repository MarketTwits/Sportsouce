package com.markettwits.club.cloud.models.schedule

import kotlinx.serialization.Serializable

@Serializable
internal data class ScheduleRemote(
    val count: Int,
    val rows: List<ScheduleRemoteRow>
)