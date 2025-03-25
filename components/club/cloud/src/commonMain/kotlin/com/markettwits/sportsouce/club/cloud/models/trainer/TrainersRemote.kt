package com.markettwits.sportsouce.club.cloud.models.trainer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TrainersRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<TrainersRemoteRow>
)