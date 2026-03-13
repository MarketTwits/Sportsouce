package com.markettwits.sportsouce.review.review.data

import com.markettwits.core_ui.items.extensions.Fifth
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.review.review.data.cache.ReviewCache
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.domain.ReviewRepository
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.starts.common.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewRepositoryBase(
    private val startsService: SportSauceStartsApi,
    private val newsService: NewsRepository,
    private val productsService: ShopCatalogRepository,
    private val cache: ReviewCache,
) : ReviewRepository {
    override suspend fun review(forced: Boolean): Flow<Review> =
        if (forced) {
            flow {
                val cached = cache.get()
                runCatching {
                    refreshReview { emit(it) }
                }.onFailure { error ->
                    if (cached != null) {
                        emit(cached)
                    } else {
                        throw error
                    }
                }
            }
        } else {
            channelFlow {
                val cacheObserver = launch {
                    cache.observe()
                        .filterNotNull()
                        .collect(::send)
                }
                val refreshJob = launch {
                    runCatching { refreshReview() }
                        .onFailure { error ->
                            if (cache.get() == null) {
                                close(error)
                            }
                        }
                }
                awaitClose {
                    cacheObserver.cancel()
                    refreshJob.cancel()
                }
            }
        }

    private suspend fun refreshReview() {
        refreshReview {}
    }

    private suspend fun refreshReview(emit: suspend (Review) -> Unit) {
        val initialReview = loadInitialReview()
        cache.set(value = initialReview)
        emit(initialReview)
        warmUpArchiveCache(initialReview, emit)
    }

    private suspend fun loadInitialReview(): Review {
        val (actual, archiveFirstPage, news, products, merchProducts) = withContext(Dispatchers.Main.immediate) {
            val deferredActual = async { startsService.fetchStartMain() }
            val deferredArchive = async { archivePagingSource().load(offset = 0, limit = STARTS_PAGE_SIZE) }
            val deferredNews = async { newsService.news().getOrThrow() }
            val deferredProducts = async { productsService.salesProducts().getOrDefault(emptyList()) }
            val deferredMerchProducts = async { productsService.merchProducts().getOrDefault(emptyList()) }
            Fifth(
                deferredActual.await(),
                deferredArchive.await(),
                deferredNews.await(),
                deferredProducts.await(),
                deferredMerchProducts.await()
            )
        }
        return Review(
            news = news,
            actualStarts = actual,
            archiveStarts = archiveFirstPage,
            products = products.reversed(),
            merchProducts = merchProducts.reversed()
        )
    }

    private suspend fun warmUpArchiveCache(
        initialReview: Review,
        emit: suspend (Review) -> Unit = {},
    ) {
        val pagingSource = archivePagingSource()
        var offset = initialReview.archiveStarts.size

        while (true) {
            val page = pagingSource.load(offset = offset, limit = STARTS_PAGE_SIZE)
            if (page.isEmpty()) {
                return
            }
            offset += page.size
            val currentReview = cache.get() ?: initialReview
            val updatedReview = currentReview.appendArchive(page)
            cache.set(value = updatedReview)
            emit(updatedReview)
            if (page.size < STARTS_PAGE_SIZE) {
                return
            }
        }
    }

    private fun archivePagingSource(): StartsPagingSource = StartsPagingSource(
        startsApi = startsService,
        params = StartsPagingParams(
            isGroup = true,
            statuses = listOf(StartStatus.ENDED),
        )
    )

    private fun Review.appendArchive(page: List<StartsListItem>): Review = copy(
        archiveStarts = (archiveStarts + page).distinctBy(StartsListItem::id)
    )
}
