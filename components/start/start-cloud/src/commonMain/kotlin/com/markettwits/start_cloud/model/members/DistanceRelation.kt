package com.markettwits.start_cloud.model.members

import kotlinx.serialization.Serializable

@Serializable
data class DistanceRelation(
    val id: Int,
    val name: String
)