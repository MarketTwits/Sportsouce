package com.markettwits.sportsouce.review

import com.markettwits.cahce.Cache
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.review.review.data.ReviewRepositoryBase
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.starts.common.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.startCoroutineUninterceptedOrReturn
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewRefreshRepositoryTest {
    @Test fun emitsDiskCacheImmediatelyThenFreshDataOnEveryCollection() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val cached = Review(actualStarts = listOf(item(1)))
            val cache = MemoryCache(cached)
            var gate = CompletableDeferred<Unit>()
            var requests = 0
            val repo = repository(cache) { gate.await(); requests++; listOf(item(requests + 1)) }
            val values = mutableListOf<Review>()
            val first = launch { repo.review().toList(values) }; runCurrent()
            assertEquals(listOf(cached), values)
            gate.complete(Unit); first.join()
            assertEquals(listOf(1, 2), values.map { it.actualStarts.single().id })
            assertEquals(values.last(), cache.value)
            gate = CompletableDeferred()
            val next = mutableListOf<Review>()
            val second = launch { repo.review().toList(next) }; runCurrent()
            assertEquals(listOf(2), next.map { it.actualStarts.single().id })
            gate.complete(Unit); second.join()
            assertEquals(listOf(2, 3), next.map { it.actualStarts.single().id })
        } finally { Dispatchers.resetMain() }
    }

    @Test fun offlineUsesCacheButSuccessfulEmptyResultClearsObsoleteCards() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val cached = Review(actualStarts = listOf(item(1)))
            val cache = MemoryCache(cached)
            var fail = true
            val repo = repository(cache) { if (fail) error("offline"); emptyList() }
            assertEquals(listOf(cached), repo.review().toList())
            assertEquals(listOf(cached), repo.review(true).toList())
            fail = false
            assertEquals(listOf(cached, Review()), repo.review().toList())
            assertEquals(Review(), cache.value)
            fail = true
            assertEquals(listOf(Review()), repo.review().toList(), "An empty cache is valid data")
            cache.clear()
            assertFailsWith<IllegalStateException> { repo.review().toList() }
        } finally { Dispatchers.resetMain() }
    }

    private fun repository(cache: MemoryCache, load: suspend () -> List<StartsListItem>): ReviewRepositoryBase {
        val starts = suspendProxy<SportSauceStartsApi> { name ->
            when (name) {
                "fetchStartMain" -> load()
                "fetchStarts" -> emptyList<StartsListItem>()
                else -> error("Unexpected $name")
            }
        }
        val news = object : NewsRepository {
            override suspend fun news(categoryId: Int?, hashtag: String?, limit: Int, offset: Int) = Result.success(emptyList<com.markettwits.sportsouce.news.common.model.NewsItem>())
            override suspend fun newsItem(id: Int): Result<com.markettwits.sportsouce.news.common.model.NewsItem> = error("Unexpected newsItem")
            override suspend fun categories() = Result.success(emptyList<com.markettwits.sportsouce.news.common.model.NewsCategory>())
            override suspend fun hashtags(limit: Int, offset: Int) = Result.success(emptyList<com.markettwits.sportsouce.news.common.model.NewsHashtag>())
        }
        val products = object : ShopCatalogRepository {
            override suspend fun salesProducts() = Result.success(emptyList<com.markettwits.sportsouce.shop.domain.model.ShopItem>())
            override suspend fun merchProducts() = Result.success(emptyList<com.markettwits.sportsouce.shop.domain.model.ShopItem>())
            override fun paddingProducts(query: String): Flow<app.cash.paging.PagingData<com.markettwits.sportsouce.shop.domain.model.ShopItem>> = error("Unexpected paging")
            override fun paddingProducts(categoryId: Int?, options: List<String>?, maxPrice: Int?, minPrice: Int?): Flow<app.cash.paging.PagingData<com.markettwits.sportsouce.shop.domain.model.ShopItem>> = error("Unexpected paging")
        }
        return ReviewRepositoryBase(starts, news, products, cache)
    }
    private inline fun <reified T> suspendProxy(crossinline call: suspend (String) -> Any?): T =
        Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, args ->
            @Suppress("UNCHECKED_CAST")
            val continuation = args.last() as Continuation<Any?>
            val block: suspend () -> Any? = { call(method.name) }
            block.startCoroutineUninterceptedOrReturn(continuation)
        } as T
    private class MemoryCache(var value: Review?) : Cache<Review> {
        override suspend fun get(key: Any) = value
        override suspend fun set(key: Any, value: Review) { this.value = value }
        override suspend fun clear() { value = null }
        override suspend fun getList(key: Any) = emptyList<Review>()
    }
    private fun item(id: Int) = StartsListItem(id, "Старт $id", "", "", StartsListItem.StatusCode(1, ""), "", "", "", false, "", views = 0)
}
