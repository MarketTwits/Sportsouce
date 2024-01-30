package com.markettwits.random_user

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val city: String,
    val coordinates: Coordinates,
    val country: String,
    val state: String,
    val street: Street,
)