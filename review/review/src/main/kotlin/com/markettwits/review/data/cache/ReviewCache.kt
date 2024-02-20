package com.markettwits.review.data.cache

import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper
import com.markettwits.review.domain.Review

class ReviewCache : InStorageSingleCache<Review>(
    storeOfWrapper(
        path = InStorageCache.path,
        fileName = "reviewStarts"
    )
)