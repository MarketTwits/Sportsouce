package com.markettwits.start.presentation.membres

import kotlinx.serialization.Serializable

@Serializable
data class StartMembersUi(
    val id: Int,
    val member: String,
    val distance: String,
    val team: String,
    val group : String,
    val city: String
)