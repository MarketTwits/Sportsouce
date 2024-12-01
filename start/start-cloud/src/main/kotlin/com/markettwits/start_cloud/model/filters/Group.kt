package com.markettwits.start_cloud.model.filters

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val count: Int,
    val id: Int,
    val value: String
)