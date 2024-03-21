package com.markettwits.cloud_clubs.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionRemoteItem(
    val name: String,
    val subscription: List<Subscription>
)