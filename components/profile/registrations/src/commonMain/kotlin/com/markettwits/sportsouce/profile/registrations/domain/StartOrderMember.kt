package com.markettwits.sportsouce.profile.registrations.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartOrderMember(
    val name : String,
    val surname : String,
    val teamName : String,
    val ageGroupName: String,
    val distanceName : String,
    val genderName : String,
    val formatName : String,
    val results: List<StartOrderMemberResult>
)