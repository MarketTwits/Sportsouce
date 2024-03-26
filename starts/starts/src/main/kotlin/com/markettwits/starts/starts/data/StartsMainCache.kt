package com.markettwits.starts.starts.data

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.starts_common.domain.StartsListItem

class StartsMainCache : InStorageSingleCache<List<List<StartsListItem>>>(
    listStoreOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "mainStarts"
    )
)