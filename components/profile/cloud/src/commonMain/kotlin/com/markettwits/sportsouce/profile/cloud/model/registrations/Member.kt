package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("age_group")
    val ageGroup: AgeGroup? = null,
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("distance_id")
    val distanceId: Int?,
    @SerialName("distance_relation")
    val distanceRelation: DistanceRelation?,
    @SerialName("results")
    val results: List<MemberResult>? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("surname")
    val surname: String? = null,
    @SerialName("team")
    val team: String? = null,
    @DeprecatedField
    val distance: String? = null,
    @DeprecatedField
    val group: String? = null,
    @DeprecatedField
    val format: String? = null,
)