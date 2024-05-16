package com.markettwits.club.cloud.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionItemsRemote(
    val name: String,
    val subscription: List<SubscriptionRemote>
)
