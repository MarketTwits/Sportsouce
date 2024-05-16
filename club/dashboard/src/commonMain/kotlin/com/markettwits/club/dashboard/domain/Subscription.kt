package com.markettwits.club.dashboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val description: String,
    val discount: Int,
    val id: Int,
    val name: String,
    val price: Int,
    val type: String,
)