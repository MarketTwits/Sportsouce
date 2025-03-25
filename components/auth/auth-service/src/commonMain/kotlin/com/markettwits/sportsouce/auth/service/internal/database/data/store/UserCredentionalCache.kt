package com.markettwits.sportsouce.auth.service.internal.database.data.store

import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.auth.service.internal.database.data.entities.CredentialRealmCache

private const val FILE_NAME = "sportsouce.auth.credential"

private val userCredentialStore = storeOfWrapper<CredentialRealmCache>(
    path = InStorageFileDirectory.path,
    fileName = FILE_NAME
)

internal class UserCredentialCache : InStorageSingleCache<CredentialRealmCache>(userCredentialStore)