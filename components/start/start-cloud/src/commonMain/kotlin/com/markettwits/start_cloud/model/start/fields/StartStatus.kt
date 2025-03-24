package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class StartStatus(
    val code: Int,
    val name: String
)