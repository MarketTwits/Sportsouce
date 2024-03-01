package com.markettwits.start_filter.start_filter.data.cache

import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper
import com.markettwits.start_filter.start_filter.domain.StartFilter

internal class FilterCache : InStorageSingleCache<StartFilter>(
    storeOfWrapper(
        path = InStorageCache.path,
        fileName = "filterCached"
    )
)