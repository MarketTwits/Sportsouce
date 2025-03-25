package com.markettwits.sportsouce.settings.api.internal

import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.settings.api.api.params.ApplicationSettings

private const val FILE_NAME = "sportsouce.settings"

internal object ApplicationSettingsCache : InStorageSingleCache<ApplicationSettings>(
    storeOfWrapper(
        InStorageFileDirectory.path,
        FILE_NAME
    )
)
