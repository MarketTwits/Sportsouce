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
    val kindOfSportId: Int? = null,
    @SerialName("isPriseDependsOnCount")
    val isPriseDependsOnCount : Boolean,
    @SerialName("maxAmount")
    val maxAmount : Int?,
    @SerialName("color")
    val color : SubscriptionColor = SubscriptionColor.BLUE,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
    @SerialName("type")
    val type: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    val order: Int = 0,
)
