package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("age_group")
    val ageGroup: AgeGroup?,
    @SerialName("birthday")
    val birthday: String,
    @SerialName("distance_id")
    val distanceId: Int?,
    @SerialName("distance_relation")
    val distanceRelation: DistanceRelation?,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String,
    @DeprecatedField
    val distance: String?,
    @DeprecatedField
    val group: String?,
    @DeprecatedField
    val format: String?,
)