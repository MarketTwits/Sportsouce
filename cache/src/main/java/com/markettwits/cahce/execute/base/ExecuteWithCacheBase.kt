package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache

class ExecuteWithCacheBase : ExecuteWithCacheAbstract() {
    override suspend fun <T> executeWithCache(
        forced: Boolean,
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: (T) -> Unit
    ) {
        if (forced)
            executeWithCacheWithForced(cache, launch, callback)
        else
            executeWithCacheWithoutForced(cache, launch, callback)
    }
}