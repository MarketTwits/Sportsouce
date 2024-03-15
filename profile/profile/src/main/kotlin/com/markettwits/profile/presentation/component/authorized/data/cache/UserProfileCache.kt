package com.markettwits.profile.presentation.component.authorized.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile

internal val userProfileCache = storeOfWrapper<UserProfile>(
    path = InStorageCacheDirectory.path,
    fileName = "UserProfileCache"
)

class UserProfileCache : InStorageSingleCache<UserProfile>(userProfileCache)