package com.markettwits.review.data.cache

import com.markettwits.cahce.domain.InStorageCache
import com.markettwits.cahce.domain.InStorageSingleCache
import com.markettwits.cahce.domain.store.storeOfWrapper
import com.markettwits.review.domain.Review

class ReviewCache : InStorageSingleCache<Review>(
    storeOfWrapper(
        path = InStorageCache.path,
        fileName = "reviewStarts"
    )
)