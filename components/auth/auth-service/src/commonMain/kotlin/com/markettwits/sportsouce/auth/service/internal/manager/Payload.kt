package com.markettwits.sportsouce.auth.service.internal.manager

import kotlinx.serialization.Serializable

@Serializable
internal data class Payload(
    val userId: Int,
    val role: String,
    val iat: Long,
    val exp: Long
)