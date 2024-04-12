package com.markettwits.settings.api.internal

import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.settings.api.api.params.ApplicationSettings

internal object ApplicationSettingsCache : InStorageSingleCache<ApplicationSettings>(
    storeOfWrapper(
        InStorageFileDirectory.path,
        "applicationStorageCache"
    )
)
