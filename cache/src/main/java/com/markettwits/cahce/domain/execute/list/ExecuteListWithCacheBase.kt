package com.markettwits.cahce.domain.execute.list

import com.markettwits.cahce.domain.Cache

class ExecuteListWithCacheBase : ExecuteListWithCacheAbstract() {
    override suspend fun <T> executeListWithCache(
        forced: Boolean,
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit
    ) {
        if (forced)
            executeListWithCacheWithForced(cache, launch, callback)
        else
            executeListWithCacheWithoutForced(cache, launch, callback)
    }
}