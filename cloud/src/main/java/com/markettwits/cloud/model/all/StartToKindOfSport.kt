package com.markettwits.sportsourcedemo.all

import kotlinx.serialization.Serializable

@Serializable
data class StartToKindOfSport(
    val id: Int,
    val kindOfSport_id: Int,
    val start_id: Int
)