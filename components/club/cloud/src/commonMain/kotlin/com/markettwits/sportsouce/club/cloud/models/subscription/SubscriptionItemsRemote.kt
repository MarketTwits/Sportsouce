package com.markettwits.sportsouce.club.cloud.models.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionItemsRemote(
    @SerialName("name")
    val name: String,
    @SerialName("isDefault")
    val isDefault : Boolean,
    @SerialName("subscription")
    val subscription: List<SubscriptionRemote>
)
