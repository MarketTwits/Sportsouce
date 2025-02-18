package com.markettwits.cahce.execute.list

import com.markettwits.cahce.Cache

class ExecuteListWithCacheBase : ExecuteListWithCacheAbstract() {

    override suspend fun <T> executeListWithCache(
        forced: Boolean,
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: suspend (List<T>) -> Unit
    ) {
        if (forced)
            executeListWithCacheWithForced(cache, launch, callback)
        else
            executeListWithCacheWithoutForced(cache, launch, callback)
    }

    override suspend fun <T> executeListWithCache(
        forced: Boolean,
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>
    ): List<T> = throw RuntimeException("do not use")
}