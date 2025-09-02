package com.markettwits.sportsouce.profile.registrations.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration

private const val FILE_NAME = "sportsouce.profile.registrations"

internal val userStartsRegistrationsCache = storeOfWrapper<List<UserRegistration>>(
    path = InStorageCacheDirectory.path,
    fileName = FILE_NAME
)

class UserStartsRegistrationsCache : InStorageSingleCache<List<UserRegistration>>(userStartsRegistrationsCache)