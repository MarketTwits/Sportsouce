package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationRemote(
    val city: String,
    val coordinates: CoordinatesRemote,
    val country: String,
    val postcode: String,
    val state: String,
    val street: StreetRemote,
    val timezone: TimezoneRemote
)