package com.markettwits.cahce.domain.execute

import com.markettwits.cahce.domain.Cache

class ExecuteWithCacheBase : ExecuteWithCache {
    override suspend fun <T> executeListWithCache(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit,
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

    override suspend fun <T> executeWithCache(
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
}