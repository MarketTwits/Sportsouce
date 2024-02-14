package com.markettwits.cahce.domain.execute.base

import com.markettwits.cahce.domain.Cache

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
            executeWithOutCacheWithForced(cache, launch, callback)
    }
}