package com.markettwits.schedule.schedule.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.starts_common.domain.StartsListItem

internal class StartsScheduleCache : InStorageSingleCache<List<StartsListItem>>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "startsScheduleCache"
    )
)