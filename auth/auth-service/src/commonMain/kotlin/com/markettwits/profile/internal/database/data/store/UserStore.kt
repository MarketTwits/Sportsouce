package com.markettwits.profile.internal.database.data.store

import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper

private val userStore =
    storeOfWrapper<User>(path = InStorageFileDirectory.path, fileName = "userInfo")
internal class UserCache : InStorageSingleCache<User>(userStore)