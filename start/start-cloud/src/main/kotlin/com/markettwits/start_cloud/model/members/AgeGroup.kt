package com.markettwits.start_cloud.model.members

import kotlinx.serialization.Serializable

@Serializable
data class AgeGroup(
    val age_from: Int,
    val age_to: Int,
    val id: Int,
    val name: String
)