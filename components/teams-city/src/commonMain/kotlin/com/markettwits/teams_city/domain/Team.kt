package com.markettwits.teams_city.domain

import kotlinx.serialization.Serializable

@Serializable
data class Team(val id: Int, val name: String)