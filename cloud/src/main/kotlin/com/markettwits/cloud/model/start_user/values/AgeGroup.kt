package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class AgeGroup(
    val age_from: Int,
    val age_reference_date: String,
    val age_to: Int,
    val distance_id: Int,
    val format: String,
    val id: Int,
    val name: String,
    val start_id: Int
)