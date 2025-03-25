package com.markettwits.sportsouce.profile.authorized.authorized.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile

private const val FILE_NAME = "sportsouce.profile.user"

internal val userProfileCache = storeOfWrapper<UserProfile>(
    path = InStorageCacheDirectory.path,
    fileName = FILE_NAME
)

class UserProfileCache : InStorageSingleCache<UserProfile>(userProfileCache)