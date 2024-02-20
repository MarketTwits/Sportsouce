package com.markettwits.review.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.review.data.cache.ReviewCache
import com.markettwits.review.data.mapper.ReviewMapper
import com.markettwits.review.domain.Review
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
    private val reviewMapper: ReviewMapper,
    private val executor: ExecuteWithCache,
) : ReviewRepository {
    private val scope = CoroutineScope(Dispatchers.Main)
    override suspend fun launch(forced: Boolean): Flow<Result<Review>> = channelFlow {
        runCatching {
            executor.executeWithCache(
                forced = forced,
                cache = cache,
                launch = ::launchs,
                callback = {
                    scope.launch { send(Result.success(it)) }
                },
            )
        }.onFailure {
            scope.launch { send(Result.failure(it)) }
        }
        awaitClose()
    }

    private suspend fun launchs(): Review {
        val (actual, archive, news) = coroutineScope {
            withContext(Dispatchers.IO) {
                val deferredActual = async { service.fetchActualStarts() }
                val deferredPaste = async { service.fetchPasteStarts() }
                val deferredNews = async { service.news() }
                Triple(deferredActual.await(), deferredPaste.await(), deferredNews.await())
            }
        }
        return reviewMapper.map(actual, archive, news)
    }
}