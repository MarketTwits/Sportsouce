package com.markettwits.profile.internal.database.data.store

import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.profile.internal.database.data.entities.CredentialRealmCache

private val userCredentialStore = storeOfWrapper<CredentialRealmCache>(
    path = InStorageFileDirectory.path,
    fileName = "userCredentialCache"
)

internal class UserCredentialCache : InStorageSingleCache<CredentialRealmCache>(userCredentialStore)