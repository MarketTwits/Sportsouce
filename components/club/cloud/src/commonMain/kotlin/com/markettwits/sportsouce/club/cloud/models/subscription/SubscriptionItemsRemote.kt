package com.markettwits.sportsouce.club.cloud.models.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionItemsRemote(
    @SerialName("name")
    val name: String = "",
    @SerialName("isDefault")
    val isDefault : Boolean = false,
    @SerialName("subscription")
    val subscription: List<SubscriptionRemote> = emptyList(),
    val id: Int? = null,
    val type: String? = null,
    val subscriptions: List<SubscriptionRemote>? = null,
    val isHidden: Boolean = false,
    val order: Int = 0,
)
