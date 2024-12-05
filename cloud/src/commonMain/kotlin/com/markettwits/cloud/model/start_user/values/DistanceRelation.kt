package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class DistanceRelation(
    val format: String,
    val id: Int,
    val name: String,
)