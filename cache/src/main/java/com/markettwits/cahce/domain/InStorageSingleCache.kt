package com.markettwits.cahce.domain

import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

abstract class InStorageSingleCache<T : @Serializable Any>(
    private val cache: KStore<T>
) : Cache<T> {
    override suspend fun get(key: Any): T? =
        cache.get()

    override suspend fun getList(key: Any): List<T> =
        emptyList()

    override suspend fun clear() {
        cache.delete()
    }

    override suspend fun set(key: Any, value: T) {
        cache.set(value)
    }

    fun observe(): Flow<T?> = cache.updates
}
