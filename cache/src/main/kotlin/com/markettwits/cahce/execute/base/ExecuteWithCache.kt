package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache
import com.markettwits.cahce.ObservableCache
import kotlinx.coroutines.flow.Flow

interface ExecuteWithCache {
    /**
     * Executes an operation with cache and the ability to observe changes
     * with forced write. On success, writes data to the cache and
     * returns the result, otherwise returns local data from the cache.
     *
     * @param forced Whether the data needs to be updated forcibly. The launch
     *     block will be executed and the result will be returned from there
     *     without using the cache. In case of an error, the data will be taken
     *     from the cache
     * @param cache The cache to perform the operation with.
     * @param launch The function to be executed when there is no data in the
     *     cache.
     * @param callback The callback to which the actual execution result will
     *     be sent
     */
    suspend fun <T> executeWithCache(
        forced: Boolean = false,
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: suspend (T) -> Unit
    )

    /**
     * Executes an operation with cache and the ability to observe changes
     * with forced write. On success, writes data to the cache and
     * returns the result, otherwise returns local data from the cache.
     *
     * @param forced Whether the data needs to be updated forcibly. The launch
     *     block will be executed and the result will be returned from there
     *     without using the cache. In case of an error, the data will be taken
     *     from the cache
     * @param cache The cache to perform the operation with.
     * @param launch The function to be executed.
     * @return A flow of data representing the result of the cache operation.
     */
    suspend fun <T> executeWithCache(
        forced: Boolean = false,
        cache: ObservableCache<T>,
        launch: suspend () -> T,
    ): Flow<T>
}
