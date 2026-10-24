package com.markettwits.sportsouce.review.review.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum

internal class ReviewPhotoAlbumsCache : InStorageSingleCache<List<ReviewPhotoAlbum>>(
    storeOfWrapper(path = InStorageCacheDirectory.path, fileName = "sportsouce.review.photo-albums")
)
