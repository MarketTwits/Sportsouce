package com.markettwits.sportsouce.review.review.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.review.review.domain.Review

private const val FILE_NAME = "sportsouce.review"

class ReviewCache : InStorageSingleCache<Review>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME
    )
)