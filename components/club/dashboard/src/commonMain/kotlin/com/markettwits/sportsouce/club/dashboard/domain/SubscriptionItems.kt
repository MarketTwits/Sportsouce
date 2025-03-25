package com.markettwits.sportsouce.club.dashboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionItems(
    val groupName: String,
    val subscription: List<Subscription>
)