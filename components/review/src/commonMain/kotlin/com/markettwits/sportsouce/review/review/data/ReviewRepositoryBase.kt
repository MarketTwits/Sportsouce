package com.markettwits.sportsouce.review.review.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.core_ui.items.extensions.Fifth
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.review.review.data.cache.ReviewCache
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.domain.ReviewRepository
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReviewRepositoryBase(
    private val startsService: SportSauceStartsApi,
    private val newsService: NewsRepository,
    private val productsService: ShopCatalogRepository,
    private val cache: ReviewCache,
    private val executor: ExecuteWithCache,
) : ReviewRepository {
    override suspend fun review(forced: Boolean): Flow<Review> = executor.executeWithCache(
        forced = forced,
        cache = cache,
        launch = this::launch,
    )
    private suspend fun launch(): Review {
        val (actual, archive, news, products, merchProducts) = coroutineScope {
            withContext(Dispatchers.Main.immediate) {
                val deferredActual = async { startsService.fetchStartMain() }
                val deferredPaste = async { startsService.fetchPasteStarts().reversed() }
                val deferredNews = async { newsService.news().getOrThrow() }
                val deferredProducts = async { productsService.salesProducts().getOrDefault(emptyList()) }
                val deferredMerchProducts = async { productsService.merchProducts().getOrDefault(emptyList()) }
                Fifth(
                    deferredActual.await(),
                    deferredPaste.await(),
                    deferredNews.await(),
                    deferredProducts.await(),
                    deferredMerchProducts.await()
                )
            }
        }
        return Review(
            news = news,
            actualStarts = actual,
            archiveStarts = archive.reversed(),
            products = products.reversed(),
            merchProducts = merchProducts.reversed()
        )
    }
}
