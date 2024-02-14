package com.markettwits.cahce.domain.execute.base

import com.markettwits.cahce.domain.Cache

abstract class ExecuteWithCacheAbstract : ExecuteWithCache {
    protected suspend fun <T> executeWithOutCacheWithForced(
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: (T) -> Unit
    ) {
        val cachedData = cache.get()
        val newData = if (cachedData == null) {
            val data = launch()
            cache.set(value = data)
            data
        } else {
            cachedData
        }
        callback(newData)
        val latestData = runCatching {
            launch()
        }
        latestData.onSuccess {
            if (it != cachedData) {
                cache.set(value = it)
                callback(it)
            }
        }
    }

    protected suspend fun <T> executeWithCacheWithForced(
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: (T) -> Unit
    ) {
        launch().also {
            if (it != cache.get()) {
                cache.set(value = it)
            }
            callback(it)
        }
    }
}