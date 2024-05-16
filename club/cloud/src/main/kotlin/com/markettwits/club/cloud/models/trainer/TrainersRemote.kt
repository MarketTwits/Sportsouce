package com.markettwits.club.cloud.models.trainer

import kotlinx.serialization.Serializable

@Serializable
data class TrainersRemote(
    val count: Int,
    val rows: List<TrainersRemoteRow>
)