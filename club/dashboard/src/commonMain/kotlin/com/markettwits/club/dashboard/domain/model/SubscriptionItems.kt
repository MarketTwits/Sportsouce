package com.markettwits.club.dashboard.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionItems(
    val groupName: String,
    val subscription: List<Subscription>
)