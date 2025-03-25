package com.markettwits.sportsouce.club.cloud.models.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionRemote(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String,
    @SerialName("discount")
    val discount: Int?,
    @SerialName("id")
    val id: Int,
    @SerialName("kindOfSportId")
    val kindOfSportId: Int,
    @SerialName("isPriseDependsOnCount")
    val isPriseDependsOnCount : Boolean,
    @SerialName("maxAmount")
    val maxAmount : Int?,
    @SerialName("color")
    val color : SubscriptionColor,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
    @SerialName("type")
    val type: String,
    @SerialName("updatedAt")
    val updatedAt: String
)