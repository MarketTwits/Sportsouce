package com.markettwits.sportsouce.profile.registrations.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartOrderMemberResult(
    val bodyNumber: String,
    val circles: Map<Int, String>,
    val distance: String,
    val id: Int,
    val memberStartId: Int,
    val userName: String,
    val place: Int,
    val result: String,
    val sex: String,
    val shift: String,
    val team: String,
)