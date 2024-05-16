package com.markettwits.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Training(
    val id: Int,
    val type: String,
    val htmlDescription: String,
    val imageUrl: String
)