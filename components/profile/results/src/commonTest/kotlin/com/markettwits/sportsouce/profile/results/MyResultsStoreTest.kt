package com.markettwits.sportsouce.profile.results

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.store.*
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.Intent
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class MyResultsStoreTest {
    @BeforeTest fun main() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun reset() { Dispatchers.resetMain() }
    @Test fun changingComparisonCancelsOldRequestAndPreservesMostRecentData() = runTest {
        val repo = FakeResults()
        val store = MyResultsStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            runCurrent()
            assertEquals("run:5000", store.state.category)
            assertTrue(store.state.progressLoading)
            store.accept(Intent.Compare(ResultComparison.Group)); runCurrent()
            assertEquals(2, repo.calls.size)
            repo.pending.last().complete(ResultProgress(compare = "group")); runCurrent()
            assertEquals("group", store.state.progress?.compare)
            repo.pending.first().complete(ResultProgress(compare = "self")); runCurrent()
            assertEquals("group", store.state.progress?.compare)
            store.accept(Intent.Category(null)); runCurrent()
            assertNull(repo.calls.last().first)
            assertNull(store.state.progress)
        } finally { store.dispose() }
    }
    @Test fun failedRefreshKeepsSummaryAndRetryRecoversWithoutTouchingHistoryFilter() = runTest {
        val repo = FakeResults()
        val store = MyResultsStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            runCurrent()
            val summary = store.state.summary
            store.accept(Intent.FeedCategory("run:5000"))
            repo.fail = true
            store.accept(Intent.Refresh); runCurrent()
            assertEquals(summary, store.state.summary)
            assertNotNull(store.state.summaryError)
            assertEquals("run:5000", store.state.feedCategory)
            repo.fail = false
            store.accept(Intent.RetrySummary); runCurrent()
            assertNull(store.state.summaryError)
            store.accept(Intent.Series("park")); runCurrent()
            assertEquals("park", repo.calls.last().third)
        } finally { store.dispose() }
    }
    @Test fun paginationKeepsTheServerFilterAndStopsAtTheLastPage() = runTest {
        val calls = mutableListOf<Pair<Int, String?>>()
        val repo = object : MyResultsRepository {
            override suspend fun series() = emptyList<ResultSeries>()
            override suspend fun summary() = ResultsSummary()
            override suspend fun progress(category: String?, comparison: ResultComparison, series: String?) = ResultProgress()
            override suspend fun feed(offset: Int, limit: Int, category: String?): ResultFeed {
                calls += offset to category
                return ResultFeed(25, (offset until minOf(offset + limit, 25)).map { MyResult("result:$it") })
            }
        }
        val counts = mutableListOf<Int>()
        val source = ResultsPagingSource(repo, "run:5000", counts::add)
        assertEquals(20, source.load(0, 20).size)
        assertEquals(5, source.load(20, 20).size)
        assertTrue(source.load(40, 20).isEmpty())
        assertEquals(listOf<Pair<Int, String?>>(0 to "run:5000", 20 to "run:5000"), calls)
        assertEquals(listOf(25, 25), counts)
    }

    private class FakeResults : MyResultsRepository {
        var fail = false
        val calls = mutableListOf<Triple<String?, ResultComparison, String?>>()
        val pending = mutableListOf<CompletableDeferred<ResultProgress>>()
        override suspend fun series() = listOf(ResultSeries("park", "Парковый кросс", 1, listOf("run:5000")))
        override suspend fun summary(): ResultsSummary {
            if (fail) error("offline")
            return ResultsSummary(1, 1, 2025, listOf(ResultCategory("run:5000", "5 км")))
        }
        override suspend fun feed(offset: Int, limit: Int, category: String?) = ResultFeed()
        override suspend fun progress(category: String?, comparison: ResultComparison, series: String?): ResultProgress {
            calls += Triple(category, comparison, series)
            return CompletableDeferred<ResultProgress>().also { pending += it }.await()
        }
    }
}
