package com.markettwits.sportsouce.start.cloud.model.members

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val additional_field_answers: List<AdditionalFieldAnswer>?,
    val age_group: AgeGroup?,
    val age_group_id: Int?,
    val city: String,
    val distance_id: Int?,
    val distance_relation: DistanceRelation,
    val gender: String,
    val id: Int,
    val name: String,
    val reg_code: String,
    val registration_date: String,
    val stage: MemberStage,
    val surname: String,
    val team: String
)