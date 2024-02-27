package com.markettwits.cahce.execute.list

import com.markettwits.cahce.Cache

abstract class ExecuteListWithCacheAbstract : ExecuteListWithCache {
    protected suspend fun <T> executeListWithCacheWithForced(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: suspend (List<T>) -> Unit
    ) {
        runCatching { launch() }
            .fold(onSuccess = {
                if (it != cache.get()) {
                    cache.set(value = it)
                    callback(it)
                }
                callback(it)
            }, onFailure = {
                val local = cache.get()
                if (local.isNullOrEmpty()) {
                    throw it
                } else {
                    callback(local)
                }
            })
    }

    protected suspend fun <T> executeListWithCacheWithoutForced(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: suspend (List<T>) -> Unit
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