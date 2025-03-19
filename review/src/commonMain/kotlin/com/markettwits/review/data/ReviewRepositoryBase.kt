package com.markettwits.review.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.news.cloud.SportSauceNewsNetworkApi
import com.markettwits.review.data.cache.ReviewCache
import com.markettwits.review.data.mapper.ReviewMapper
import com.markettwits.review.domain.Review
import com.markettwits.review.domain.ReviewRepository
import com.markettwits.starts_common.domain.SportSauceStartsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReviewRepositoryBase(
    private val startsService: SportSauceStartsApi,
    private val newsService : SportSauceNewsNetworkApi,
    private val cache: ReviewCache,
    private val reviewMapper: ReviewMapper,
    private val executor: ExecuteWithCache,
) : ReviewRepository {
    override suspend fun review(forced: Boolean): Flow<Review> = executor.executeWithCache(
            forced = forced,
            cache = cache,
            launch = this::launch,
        )

    private suspend fun launch(): Review {
        val (actual, archive, news) = coroutineScope {
            withContext(Dispatchers.Main.immediate) {
                val deferredActual = async { startsService.fetchStartMain() }
                val deferredPaste = async { startsService.fetchPasteStarts() }
                val deferredNews = async { newsService.news() }
                Triple(deferredActual.await(), deferredPaste.await(), deferredNews.await())
            }
        }
        return reviewMapper.map(actual, archive, news)
    }
}