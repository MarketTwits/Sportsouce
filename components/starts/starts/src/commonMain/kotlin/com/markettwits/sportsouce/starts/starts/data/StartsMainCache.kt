package com.markettwits.sportsouce.starts.starts.data

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

private const val FILE_NAME = "sportsouce.starts.grouplist"

internal class StartsMainCache : InStorageSingleCache<List<List<StartsListItem>>>(
    listStoreOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME
    )
)