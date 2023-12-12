package com.markettwits.sportsourcedemo.all

import kotlinx.serialization.Serializable

@Serializable
data class StartStatus(
    val code: Int,
    val name: String
)