package com.markettwits.cloud_clubs.models.trainer

import kotlinx.serialization.Serializable

@Serializable
data class TrainersRemote(
    val count: Int,
    val rows: List<TrainersRemoteRow>
)