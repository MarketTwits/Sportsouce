package com.markettwits.sportsouce.club.dashboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val description: String,
    val discount: Int,
    val id: Int,
    val name: String,
    val price: Int,
    val priceDependsOnCount : Boolean,
    val maxAmount : Int,
    val type: String,
)