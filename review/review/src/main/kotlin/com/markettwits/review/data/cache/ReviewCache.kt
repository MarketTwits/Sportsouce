package com.markettwits.review.data.cache

import com.markettwits.cahce.domain.InStorageCache
import com.markettwits.cahce.domain.InStorageSingleCache
import com.markettwits.cahce.domain.store.listStoreOfWrapper
import com.markettwits.starts_common.domain.StartsListItem

class ReviewCache : InStorageSingleCache<List<List<StartsListItem>>>(
    listStoreOfWrapper(
        path = InStorageCache.path,
        fileName = "reviewStarts"
    )
)