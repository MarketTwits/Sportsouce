package com.markettwits.sportsouce.review.review.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.review.review.data.cache.ReviewCache
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.domain.ReviewRepository
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReviewRepositoryBase(
    private val startsService: SportSauceStartsApi,
    private val newsService: NewsRepository,
    private val cache: ReviewCache,
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
                val deferredNews = async { newsService.news().getOrThrow() }
                Triple(deferredActual.await(), deferredPaste.await(), deferredNews.await())
            }
        }
        return Review(news, actual, archive.reversed())
    }
}