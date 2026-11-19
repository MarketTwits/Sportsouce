package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class Archive(
    val id: Int,
    val name: String = "",
    val description: String = "",
)
