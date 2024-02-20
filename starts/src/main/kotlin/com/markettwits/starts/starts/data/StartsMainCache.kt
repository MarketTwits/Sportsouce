package com.markettwits.starts.starts.data

import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.listStoreOfWrapper
import com.markettwits.starts_common.domain.StartsListItem

class StartsMainCache : InStorageSingleCache<List<List<StartsListItem>>>(
    listStoreOfWrapper(
        path = InStorageCache.path,
        fileName = "mainStarts"
    )
)