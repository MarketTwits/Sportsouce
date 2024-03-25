package com.markettwits.schedule.schedule.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper

internal class StartsScheduleCache : InStorageSingleCache<StartsScheduleCacheContent>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "startsScheduleCache"
    )
)