package com.markettwits.teams_city.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper

internal class TeamsAndCitiesCache : InStorageSingleCache<TeamsAndCities>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "cities&teams"
    )
)