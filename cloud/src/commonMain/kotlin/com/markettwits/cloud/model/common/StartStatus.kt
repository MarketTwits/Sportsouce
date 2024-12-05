package com.markettwits.cloud.model.common

import kotlinx.serialization.Serializable

@Serializable
data class StartStatus(
    val code: Int,
    val name: String
)