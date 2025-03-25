package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class Season(
    val id: Int,
    val name: String
)