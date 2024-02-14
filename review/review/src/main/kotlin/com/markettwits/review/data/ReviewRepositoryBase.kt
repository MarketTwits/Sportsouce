package com.markettwits.review.data

import com.markettwits.cahce.domain.execute.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.review.data.cache.ReviewCache
import com.markettwits.starts_common.data.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewRepositoryBase(
    private val service: SportsouceApi,
    private val cache: ReviewCache,
    private val mapper: StartsCloudToListMapper,
    private val executor: ExecuteWithCache,
) : ReviewRepository {
    private val scope = CoroutineScope(Dispatchers.Main)
    override suspend fun launch(): Flow<Result<List<List<StartsListItem>>>> = channelFlow {
        runCatching {
            executor.executeListWithCache(
                cache,
                ::launchs,
                callback = {
                    scope.launch { send(Result.success(it)) }
                },
            )
        }.onFailure {
            scope.launch { send(Result.failure(it)) }
        }
        awaitClose()
    }

    private suspend fun launchs(): List<List<StartsListItem>> {
        val (actual, archive) = coroutineScope {
            withContext(Dispatchers.IO) {
                val deferredActual = async { service.fetchActualStarts() }
                val deferredPaste = async { service.fetchPasteStarts() }
                Pair(deferredActual.await(), deferredPaste.await())
            }
        }
        val actualList = mapper.mapSingle(actual.rows)
        val archiveList = mapper.mapSingle(archive.rows)
        return listOf(actualList, archiveList)
    }
}