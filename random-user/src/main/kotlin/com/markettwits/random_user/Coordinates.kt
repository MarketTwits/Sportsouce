package com.markettwits.random_user

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val latitude: String,
    val longitude: String
)