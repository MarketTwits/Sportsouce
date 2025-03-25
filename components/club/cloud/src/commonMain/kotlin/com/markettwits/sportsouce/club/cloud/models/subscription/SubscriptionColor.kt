package com.markettwits.sportsouce.club.cloud.models.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubscriptionColor{
    @SerialName("blue") BLUE,
    @SerialName("green") GREEN,
    @SerialName("purple") PURPLE,
    @SerialName("orange") ORANGE,
}