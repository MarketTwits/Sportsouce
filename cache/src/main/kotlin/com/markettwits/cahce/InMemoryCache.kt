package com.markettwits.cahce

abstract class InMemoryCache<T> : Cache<T> {
    private val data = HashMap<Any, T>()

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

    override suspend fun set(key: Any, value: T) {
        data[key] = value
    }

    override suspend fun clear() {
        data.clear()
    }
}