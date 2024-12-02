package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val age_group: AgeGroup?,
    val birthday: String,
    val distance: String?,
    val distance_id: Int?,
    val distance_relation: DistanceRelation?,
    val format: String?,
    val gender: String,
    val group: String?,
    val id: Int,
    val name: String,
    val surname: String,
    val team: String
)