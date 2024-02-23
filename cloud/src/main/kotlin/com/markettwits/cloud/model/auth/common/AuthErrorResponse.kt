package com.markettwits.cloud.model.auth.common

import kotlinx.serialization.Serializable

@Serializable
data class AuthErrorResponse(
    val message: String,
    val path: String,
    val statusCode: Int,
    val timestamp: String
)