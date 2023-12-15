package com.markettwits.cloud.model.start

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String
)