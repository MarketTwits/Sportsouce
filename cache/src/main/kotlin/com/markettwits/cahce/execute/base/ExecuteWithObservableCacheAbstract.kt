package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache
import com.markettwits.cahce.ObservableCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class ExecuteWithObservableCacheAbstract : ExecuteWithCacheAbstract() {
    protected suspend fun <T> executeObservableCacheWithoutForced(
        cache: ObservableCache<T>,
        launch: suspend () -> T,
    ): Flow<T> = flow {
        val cachedData = cache.observe()
        cachedData.collect {
            val newData = if (it == null) {
                val data = launch()
                cache.set(value = data)
                data
            } else {
                it
            }
            emit(newData)
        }
        val latestData = runCatching {
            launch()
        }
        latestData.onSuccess {
            if (it != cachedData) {
                cache.set(value = it)
                emit(it)
            }
        }
    }

    protected suspend fun <T> executeObservableCacheWithForced(
        cache: Cache<T>,
        launch: suspend () -> T,
    ): Flow<T> = flow {
        runCatching { launch() }
            .fold(onSuccess = {
                if (it != cache.get()) {
                    cache.set(value = it)
                    emit(it)
                }
                emit(it)
            }, onFailure = {
                val local = cache.get()
                if (local == null) {
                    throw it
                } else {
                    emit(local)
                }
            })
    }
}