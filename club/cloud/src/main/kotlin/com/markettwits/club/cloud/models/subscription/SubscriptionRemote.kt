package com.markettwits.club.cloud.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionRemote(
    val createdAt: String,
    val description: String,
    val discount: Int?,
    val id: Int,
    val kindOfSportId: Int,
    val isPriseDependsOnCount : Boolean,
    val maxAmount : Int?,
    val color : SubscriptionColor,
    val name: String,
    val price: Int,
    val type: String,
    val updatedAt: String
)