package com.markettwits.club.dashboard.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote


internal class ClubSubscriptionsCache : InStorageSingleCache<List<SubscriptionItemsRemote>>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "clubSubscriptionCache"
    )
)