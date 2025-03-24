package com.markettwits.review.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.review.domain.Review

class ReviewCache : InStorageSingleCache<Review>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "reviewStarts"
    )
)