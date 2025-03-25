package com.markettwits.sportsouce.start.filter.start_filter.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.start.filter.start_filter.domain.StartFilter

private const val FILE_NAME = "sportsouce.start.filter"

internal class FilterCache : InStorageSingleCache<StartFilter>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME
    )
)