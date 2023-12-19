package com.markettwits.start.presentation.membres

import kotlinx.serialization.Serializable

@Serializable
data class StartMembersUi(
    val id: Int,
    val name: String,
    val surname : String,
    val distance: String,
    val team: String,
    val group : String,
    val city: String
)