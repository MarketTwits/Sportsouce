package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache
import com.markettwits.cahce.ObservableCache

class ExecuteWithCacheBase : ExecuteWithObservableCacheAbstract() {
    override suspend fun <T> executeWithCache(
        forced: Boolean,
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: suspend (T) -> Unit
    ) {
        if (forced)
            executeWithCacheWithForced(cache, launch, callback)
        else
            executeWithCacheWithoutForced(cache, launch, callback)
    }

    override suspend fun <T> executeWithCache(
        forced: Boolean,
        cache: ObservableCache<T>,
        launch: suspend () -> T,
    ) = if (forced)
        executeObservableCacheWithForced(cache, launch)
    else
        executeObservableCacheWithoutForced(cache, launch)
}