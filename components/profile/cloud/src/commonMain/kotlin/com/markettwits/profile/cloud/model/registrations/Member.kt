package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val age_group: AgeGroup?,
    val birthday: String,
    val distance_id: Int?,
    val distance_relation: DistanceRelation?,
    val gender: String,
    val id: Int,
    val name: String,
    val surname: String,
    val team: String,
    @DeprecatedField
    val distance: String?,
    @DeprecatedField
    val group: String?,
    @DeprecatedField
    val format: String?,
)