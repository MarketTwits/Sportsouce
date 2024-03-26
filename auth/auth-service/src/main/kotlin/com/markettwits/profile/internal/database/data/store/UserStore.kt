package com.markettwits.profile.internal.database.data.store

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.cloud.model.auth.sign_in.response.User

private val userStore =
    storeOfWrapper<User>(path = InStorageCacheDirectory.path, fileName = "userInfo")
internal class UserCache : InStorageSingleCache<User>(userStore)