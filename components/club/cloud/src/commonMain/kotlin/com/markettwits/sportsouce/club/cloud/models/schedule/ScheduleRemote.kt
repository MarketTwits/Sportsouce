package com.markettwits.sportsouce.club.cloud.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ScheduleRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<ScheduleRemoteRow>
)