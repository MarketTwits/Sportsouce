package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
data class DistanceRelation(
    val format: String,
    val id: Int,
    val name: String,
)