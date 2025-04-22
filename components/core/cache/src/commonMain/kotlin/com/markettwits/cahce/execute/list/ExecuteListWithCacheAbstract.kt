package com.markettwits.cahce.execute.list

import com.markettwits.cahce.Cache
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException

abstract class ExecuteListWithCacheAbstract : ExecuteListWithCache {
    @OptIn(ExperimentalSerializationApi::class)
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
                if (it is SerializationException || it is MissingFieldException) {
                    cache.clear()
                }
                if (local.isNullOrEmpty()) {
                    throw it
                } else {
                    callback(local)
                }
            })
    }

    @OptIn(ExperimentalSerializationApi::class)
    protected suspend fun <T> executeListWithCacheWithoutForced(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: suspend (List<T>) -> Unit
    ) {
        runCatching {
            cache.get()
        }.onFailure {
            if (it is SerializationException || it is MissingFieldException) {
                cache.clear()
            }
        }.onSuccess { cachedData->
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
            latestData.onFailure {
                if (it is SerializationException || it is MissingFieldException) {
                    cache.clear()
                }
            }
        }
    }
}