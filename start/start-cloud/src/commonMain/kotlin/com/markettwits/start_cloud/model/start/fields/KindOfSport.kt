package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    val id: Int,
    val name: String
)