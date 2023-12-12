package com.markettwits.sportsourcedemo.all

import kotlinx.serialization.Serializable

@Serializable
data class Season(
    val id: Int,
    val name: String
)