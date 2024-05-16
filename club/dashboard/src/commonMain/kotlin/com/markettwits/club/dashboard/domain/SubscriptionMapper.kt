package com.markettwits.club.dashboard.domain

import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.dashboard.domain.model.SubscriptionItems

interface SubscriptionMapper {
    fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems>
}