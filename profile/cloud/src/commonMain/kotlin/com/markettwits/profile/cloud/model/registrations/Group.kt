package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val name: String,
    val sex: String? = null,
    val ageFrom: String,
    val ageTo: String,
    val stages: List<Stage>? = null
)