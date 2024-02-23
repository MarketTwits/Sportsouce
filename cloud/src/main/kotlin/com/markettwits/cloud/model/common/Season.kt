package com.markettwits.cloud.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Season(
    val id: Int,
    val name: String
)