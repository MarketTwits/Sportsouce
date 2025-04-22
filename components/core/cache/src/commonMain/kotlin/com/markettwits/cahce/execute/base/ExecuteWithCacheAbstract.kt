package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException

abstract class ExecuteWithCacheAbstract : ExecuteWithCache {
    @OptIn(ExperimentalSerializationApi::class)
    protected suspend fun <T> executeWithCacheWithoutForced(
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: suspend (T) -> Unit
    ) {
        runCatching {
            cache.get()
        }.onFailure {
            if (it is SerializationException || it is MissingFieldException) {
                cache.clear()
            }
        }.onSuccess { cachedData ->
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
            latestData.onFailure {
                if (it is SerializationException || it is MissingFieldException) {
                    cache.clear()
                }
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    protected suspend fun <T> executeWithCacheWithForced(
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: suspend (T) -> Unit
    ) {
        runCatching { launch() }
            .fold(onSuccess = {
                if (it != cache.get()) {
                    cache.set(value = it)
                    callback(it)
                }
                callback(it)
            }, onFailure = {
                if (it is SerializationException || it is MissingFieldException) {
                    cache.clear()
                }
                val local = cache.get()
                if (local == null) {
                    throw it
                } else {
                    callback(local)
                }
            })
    }
}