package com.markettwits.profile.authorized.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.profile.authorized.domain.UserProfile

internal val userProfileCache = storeOfWrapper<UserProfile>(
    path = InStorageCacheDirectory.path,
    fileName = "UserProfileCache"
)

class UserProfileCache : InStorageSingleCache<UserProfile>(userProfileCache)