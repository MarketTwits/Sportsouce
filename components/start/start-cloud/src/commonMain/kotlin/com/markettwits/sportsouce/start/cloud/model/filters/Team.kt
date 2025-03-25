package com.markettwits.sportsouce.start.cloud.model.filters

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val count: Int,
    val value: String
)