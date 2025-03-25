package com.markettwits.sportsouce.auth.service.internal.database.data.store

import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User

private const val FILE_NAME = "sportsouce.auth.user.info"

private val userStore =
    storeOfWrapper<User>(path = InStorageFileDirectory.path, fileName = FILE_NAME)

internal class UserCache : InStorageSingleCache<User>(userStore)