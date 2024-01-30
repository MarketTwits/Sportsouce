package com.markettwits.random_user.api.model

import kotlinx.serialization.Serializable

@Serializable
data class InfoRemote(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)