package com.markettwits.start_filter.start_filter.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.start_filter.start_filter.domain.StartFilter

internal class FilterCache : InStorageSingleCache<StartFilter>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "filterCached"
    )
)