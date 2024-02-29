package com.markettwits.teams_city.data.cache

import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.storeOfWrapper

internal class TeamsAndCitiesCache : InStorageSingleCache<TeamsAndCities>(
    storeOfWrapper(
        path = InStorageCache.path,
        fileName = "cities&teams"
    )
)