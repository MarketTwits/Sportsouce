package com.markettwits.cloud.model.starts

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String
)