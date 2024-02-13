package com.markettwits.cahce.domain.execute

import com.markettwits.cahce.domain.Cache

class ExecuteWithCacheBase : ExecuteWithCache {
    override suspend fun <T> executeListWithCache(
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
        val latestData = launch()
        if (latestData != cachedData) {
            cache.set(value = latestData)
            callback(latestData)
        }
    }
}