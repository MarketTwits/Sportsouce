package com.markettwits.cahce

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.extensions.getOrEmpty
import io.github.xxfast.kstore.extensions.minus
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

abstract class InStorageListCache<T : @Serializable Any>(
    private val cache: KStore<List<T>>
) : Cache<T> {
    override suspend fun get(key: Any): T? =
        (cache.get() ?: emptyList<T>()) as T?

    override suspend fun getList(key: Any): List<T> =
        cache.getOrEmpty()

    override suspend fun clear() {
        cache.delete()
    }

    override suspend fun set(key: Any, value: T) {
        cache.update {
            it?.plus(value)
        }
    }

    suspend fun remove(key: Any = Unit, value: T) {
        cache.minus(value)
    }

    fun observe(): Flow<List<T>?> =
        cache.updates
}
