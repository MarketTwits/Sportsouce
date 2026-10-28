package com.markettwits.sportsouce.starts.starts

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.*
import com.markettwits.cahce.Cache
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.starts.common.domain.*
import com.markettwits.sportsouce.starts.starts.data.*
import com.markettwits.sportsouce.starts.starts.presentation.component.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.test.*
import kotlin.coroutines.intrinsics.startCoroutineUninterceptedOrReturn

@OptIn(ExperimentalCoroutinesApi::class)
class StartsRefreshTest {
    @Test fun cachedTabRefreshesOnEveryVisitWithoutPullIndicatorAndManualRefreshStillShowsIt() = runTest {
        val cache = MemoryCache(StartsTabsCacheModel(actual = listOf(item(1))))
        var gate = CompletableDeferred<Unit>()
        var calls = 0
        val repository = repository(cache) { _, offset ->
            assertEquals(0, offset)
            calls++
            gate.await()
            listOf(item(calls + 1))
        }
        val first = launch { repository.starts(false, 1) }
        runCurrent()
        assertEquals(listOf(1), repository.state().tabs[1].items.map { it.id })
        assertFalse(repository.state().isRefreshing)
        // Repeated signals while the same request is pending must not duplicate it.
        repository.starts(false, 1)
        assertEquals(1, calls)
        repository.starts(true, 1)
        assertTrue(repository.state().isRefreshing, "A manual pull can join an in-flight background refresh")
        assertEquals(1, calls)
        gate.complete(Unit); first.join()
        assertFalse(repository.state().isRefreshing)
        assertEquals(listOf(2), repository.state().tabs[1].items.map { it.id })
        gate = CompletableDeferred()
        val second = launch { repository.starts(false, 1) }; runCurrent()
        assertEquals(2, calls, "Initialized tabs must revalidate")
        assertEquals(listOf(2), repository.state().tabs[1].items.map { it.id })
        assertFalse(repository.state().isRefreshing)
        gate.complete(Unit); second.join()
        gate = CompletableDeferred()
        val manual = launch { repository.starts(true, 1) }; runCurrent()
        assertTrue(repository.state().isRefreshing)
        gate.complete(Unit); manual.join()
        assertFalse(repository.state().isRefreshing)
        assertEquals(listOf(4), cache.value!!.actual.map { it.id })
    }

    @Test fun parallelTabsMergeIntoLatestStateAndCacheWithoutLosingPagination() = runTest {
        val cache = MemoryCache(StartsTabsCacheModel(actual = (1..20).map(::item), past = listOf(item(50))))
        val actualGate = CompletableDeferred<Unit>()
        val pastGate = CompletableDeferred<Unit>()
        val repository = repository(cache) { statuses, offset ->
            if (StartStatus.ENDED in statuses) {
                pastGate.await(); listOf(item(51))
            } else {
                actualGate.await()
                if (offset == 0) (1..20).map(::item) else listOf(item(21))
            }
        }
        val actual = launch { repository.starts(true, 1) }
        val past = launch { repository.starts(false, 2) }; runCurrent()
        pastGate.complete(Unit); past.join()
        assertTrue(repository.state().tabs[1].isLoading)
        assertTrue(repository.state().isRefreshing, "Another tab finishing must not hide manual refresh")
        actualGate.complete(Unit); actual.join()
        repository.loadNext(1)
        assertEquals((1..21).toList(), repository.state().tabs[1].items.map { it.id })
        assertEquals(listOf(51), repository.state().tabs[2].items.map { it.id })
        assertEquals(repository.state().tabs[1].items, cache.value!!.actual)
        assertEquals(repository.state().tabs[2].items, cache.value!!.past)
        assertFalse(repository.state().tabs[1].isAppending)
    }

    @Test fun offlineRefreshAndCancellationKeepCachedCardsAndAllowRetry() = runTest {
        val cache = MemoryCache(StartsTabsCacheModel(actual = listOf(item(1))))
        var fail = true
        val gate = CompletableDeferred<Unit>()
        val repository = repository(cache) { _, _ ->
            if (fail) error("offline")
            gate.await(); listOf(item(2))
        }
        repository.starts(false, 1)
        assertEquals(listOf(1), repository.state().tabs[1].items.map { it.id })
        assertNull(repository.state().tabs[1].error)
        fail = false
        val cancelled = launch { repository.starts(true, 1) }; runCurrent()
        cancelled.cancelAndJoin()
        assertFalse(repository.state().isRefreshing)
        assertFalse(repository.state().tabs[1].isLoading)
        gate.complete(Unit)
        repository.starts(false, 1)
        assertEquals(listOf(2), repository.state().tabs[1].items.map { it.id })
    }

    @Test fun refreshingAScrolledTabRevalidatesItsLoadedWindowWithoutDroppingPages() = runTest {
        val cache = MemoryCache(StartsTabsCacheModel(actual = (1..40).map(::item)))
        val limits = mutableListOf<Int>()
        val repository = repository(cache, limits) { _, offset ->
            assertEquals(0, offset)
            (1..40).map { item(it).copy(name = "Обновлённый старт $it") }
        }
        repository.starts(false, 1)
        assertEquals(listOf(40), limits)
        assertEquals(40, repository.state().tabs[1].items.size)
        assertTrue(repository.state().tabs[1].items.all { it.name.startsWith("Обновлённый") })
    }

    @Test fun oneTabFailingMustNotDiscardAnotherTabsSuccessfulResponse() = runTest {
        val cache = MemoryCache(null)
        val actualGate = CompletableDeferred<Unit>()
        val repository = repository(cache) { statuses, _ ->
            if (StartStatus.ENDED in statuses) error("offline")
            actualGate.await(); listOf(item(1))
        }
        val actual = launch { repository.starts(false, 1) }; runCurrent()
        repository.starts(false, 2)
        actualGate.complete(Unit); actual.join()
        assertEquals(listOf(1), repository.state().tabs[1].items.map { it.id })
        assertFalse(repository.state().tabs[1].isLoading)
    }

    @Test fun returningToScreenRefreshesSelectedTabAndDestroyUnsubscribes() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val requests = mutableListOf<Pair<Boolean, Int>>()
            val source = object : com.markettwits.sportsouce.starts.starts.domain.StartsRepository {
                override val starts = com.arkivanov.decompose.value.MutableValue<StartsUiState>(StartsUiState.Loading)
                override suspend fun starts(forced: Boolean, page: Int) { requests += forced to page }
            }
            val lifecycle = LifecycleRegistry()
            val component = StartsScreenComponent(DefaultComponentContext(lifecycle), source, {}, {}, {})
            lifecycle.resume(); runCurrent()
            component.onPageSelected(1); runCurrent()
            assertEquals(listOf(false to 1), requests)
            component.onPageSelected(2); runCurrent()
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(listOf(false to 1, false to 2, false to 2), requests)
            lifecycle.destroy()
            source.starts.value = StartsUiState.Success()
            assertEquals(StartsUiState.Loading, component.starts.value, "Destroyed keeper must release repository subscription")
        } finally { Dispatchers.resetMain() }
    }

    private fun StartsRepositoryBase.state() = starts.value as StartsUiState.Success
    private fun repository(cache: MemoryCache, limits: MutableList<Int> = mutableListOf(), load: suspend (List<StartStatus>, Int) -> List<StartsListItem>): StartsRepositoryBase {
        val api = Proxy.newProxyInstance(SportSauceStartsApi::class.java.classLoader, arrayOf(SportSauceStartsApi::class.java)) { _, method, args ->
            assertEquals("fetchStarts", method.name)
            limits += args[0] as Int
            @Suppress("UNCHECKED_CAST")
            val continuation = args.last() as kotlin.coroutines.Continuation<List<StartsListItem>>
            @Suppress("UNCHECKED_CAST")
            val block: suspend () -> List<StartsListItem> = { load(args[5] as List<StartStatus>, args[1] as Int) }
            block.startCoroutineUninterceptedOrReturn(continuation)
        } as SportSauceStartsApi
        val tracker = Proxy.newProxyInstance(ExceptionTracker::class.java.classLoader, arrayOf(ExceptionTracker::class.java)) { _, _, _ -> null } as ExceptionTracker
        return StartsRepositoryBase(api, cache, tracker)
    }
    private class MemoryCache(var value: StartsTabsCacheModel?) : Cache<StartsTabsCacheModel> {
        override suspend fun get(key: Any) = value
        override suspend fun set(key: Any, value: StartsTabsCacheModel) { yield(); this.value = value }
        override suspend fun clear() { value = null }
        override suspend fun getList(key: Any) = emptyList<StartsTabsCacheModel>()
    }
    private fun item(id: Int) = StartsListItem(id, "Старт $id", "", "", StartsListItem.StatusCode(1, ""), "", "", "", false, "", views = 0)
}
