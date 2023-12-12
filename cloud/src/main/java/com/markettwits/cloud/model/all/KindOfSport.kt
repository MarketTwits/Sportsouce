package com.markettwits.sportsourcedemo.all

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val StartToKindOfSport: StartToKindOfSport,
    val id: Int,
    val name: String
)