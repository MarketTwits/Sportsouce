package com.markettwits.cahce.domain

interface Cache<T> {
    suspend fun get(key: Any = Unit): T?
    suspend fun getList(key: Any = Unit): List<T>
    suspend fun set(key: Any = Unit, value: T)
    suspend fun clear()
}
