package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class AgeGroup(
    val age_from: Int?,
    val age_to: Int?,
    val format: String,
    val id: Int,
    val name: String,
    val start_id: Int?
)