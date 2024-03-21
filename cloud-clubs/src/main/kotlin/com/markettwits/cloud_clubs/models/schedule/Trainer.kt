package com.markettwits.cloud_clubs.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class Trainer(
    val id: Int,
    val name: String,
    val surname: String
)