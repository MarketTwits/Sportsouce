package com.markettwits.cahce

import kotlinx.coroutines.flow.Flow

interface Cache<T> {
    suspend fun get(key: Any = Unit): T?
    suspend fun getList(key: Any = Unit): List<T>
    suspend fun set(key: Any = Unit, value: T)
    suspend fun clear()
}

interface ObservableCache<T> : Cache<T> {
    fun observe(): Flow<T?>
}
