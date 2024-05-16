package com.markettwits.club.dashboard.data

import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.cloud.models.subscription.SubscriptionRemote
import com.markettwits.club.dashboard.domain.model.SubscriptionItems
import com.markettwits.club.dashboard.domain.SubscriptionMapper
import com.markettwits.club.dashboard.domain.model.Subscription

class SubscriptionMapperBase : SubscriptionMapper {
    override fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems> =
        subscriptionsRemote.map {
            SubscriptionItems(
                it.name,
                mapSubscriptions(it.subscription)
            )
        }.filter { it.subscription.isNotEmpty() }

    private fun mapSubscriptions(
        subscriptionsRemote: List<SubscriptionRemote>
    ): List<Subscription> = subscriptionsRemote.map {
        Subscription(
            description = it.description,
            discount = it.discount,
            name = it.name,
            id = it.id,
            type = it.type,
            price = it.price
        )
    }
}