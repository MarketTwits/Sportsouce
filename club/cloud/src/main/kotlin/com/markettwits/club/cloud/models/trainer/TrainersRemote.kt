package com.markettwits.club.cloud.models.trainer

import kotlinx.serialization.Serializable

@Serializable
internal data class TrainersRemote(
    val count: Int,
    val rows: List<TrainersRemoteRow>
)