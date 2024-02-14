package com.markettwits.cahce.domain.execute.list

import com.markettwits.cahce.domain.Cache

abstract class ExecuteListWithCacheAbstract : ExecuteListWithCache {
    protected suspend fun <T> executeListWithCacheWithForced(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit
    ) {
        launch().also {
            if (it != cache.get()) {
                cache.set(value = it)
                callback(it)
            }
            callback(it)
        }
    }

    protected suspend fun <T> executeListWithCacheWithoutForced(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit
    ) {
        val cachedData = cache.get()
        val newData = if (cachedData.isNullOrEmpty()) {
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
}