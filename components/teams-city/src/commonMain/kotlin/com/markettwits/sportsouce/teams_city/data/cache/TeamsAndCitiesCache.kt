package com.markettwits.sportsouce.teams_city.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper

private const val FILE_NAME = "sportsouce.cities&teams"

internal class TeamsAndCitiesCache : InStorageSingleCache<TeamsAndCities>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME
    )
)