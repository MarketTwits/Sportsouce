package com.markettwits.profile.data.database.data.store

import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper
import com.markettwits.cloud.model.auth.sign_in.response.User

val userStore = storeOfWrapper<User>(path = InStorageCache.path, fileName = "userInfo")

class UserCache : InStorageSingleCache<User>(userStore)