package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("additional_field_answers")
    val additionalFieldAnswers: List<AdditionalFieldAnswer>?,
    @SerialName("age_group")
    val ageGroup: AgeGroup?,
    @SerialName("age_group_id")
    val ageGroupId: Int?,
    @SerialName("city")
    val city: String,
    @SerialName("distance_id")
    val distanceId: Int?,
    @SerialName("distance_relation")
    val distanceRelation: DistanceRelation,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("reg_code")
    val regCode: String,
    @SerialName("registration_date")
    val registrationDate: String,
    @SerialName("stage")
    val stage: MemberStage,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String
)