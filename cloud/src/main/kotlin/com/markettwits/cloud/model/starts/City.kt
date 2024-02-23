package com.markettwits.sportsourcedemo.all

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String
)