package com.markettwits.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Statistic(
    val id: Int,
    val value: String,
    val title: String
)