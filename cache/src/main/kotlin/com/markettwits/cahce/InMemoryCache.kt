package com.markettwits.cahce

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class InMemoryCache<T> : ObservableCache<T> {

    private val data = HashMap<Any, T>()

    private val sharedFlow = MutableSharedFlow<T?>()

    override suspend fun get(key: Any): T? {
        return data[key]
    }

    override suspend fun getList(key: Any): List<T> {
        val list = mutableListOf<T>()
        for ((k, v) in data) {
            if (k == key) {
                list.add(v)
            }
        }
        return list
    }

    override fun observe(): Flow<T?> = sharedFlow

    override suspend fun set(key: Any, value: T) {
        data[key] = value
        sharedFlow.emit(value)
    }

    override suspend fun clear() {
        data.clear()
        sharedFlow.emit(null)
    }
}