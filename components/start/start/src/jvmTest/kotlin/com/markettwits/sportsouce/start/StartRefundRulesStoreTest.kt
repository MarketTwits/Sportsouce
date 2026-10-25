package com.markettwits.sportsouce.start

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.domain.*
import com.markettwits.sportsouce.start.domain.mapper.StartsListItemToStartItemMapper
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class StartRefundRulesStoreTest {
    private inline fun <reified T> unused(): T = Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
        error("Unexpected ${method.name}")
    } as T

    @Test fun policyLoadsIndependentlyRetriesInIsolationAndRefreshesEvenWhenDetailsAreCached() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val card = StartsListItem(318, "Страшно добрый забег", "", "25 октября 2026",
            StartsListItem.StatusCode(3, "Регистрация"), "", "", "", false, "4 км", views = 1)
        val details = StartsListItemToStartItemMapper.mapToPartialStartItem(card)
        val custom = StartRefundRules(318, "25 октября 2026", StartRefundRules.Source.Start,
            listOf(StartRefundRules.Rule(1, 50, 50, "Индивидуальные условия")))
        var result: Result<StartRefundRules> = Result.failure(IllegalStateException("offline"))
        val pending = CompletableDeferred<Unit>()
        var detailsLoads = 0
        var ruleLoads = 0
        val repository = object : StartRepository by unused<StartRepository>() {
            override suspend fun start(startId: String, relaunch: Boolean): Result<StartItem> { detailsLoads++; return Result.success(details) }
            override suspend fun startsRecommended(startId: String) = Result.success(emptyList<StartsListItem>())
            override suspend fun isStartInFavorite(startId: String) = Result.success(false)
            override suspend fun refundRules(startId: Int): Result<StartRefundRules> {
                assertEquals(318, startId)
                ruleLoads++
                pending.await()
                return result
            }
        }
        val store = StartScreenStoreFactory(DefaultStoreFactory(), repository, unused<ExceptionTracker>(), unused<IntentAction>())
            .create(StartScreenInput.Slug("strashno-dobryj-zabeg-318"))
        try {
            runCurrent()
            assertEquals(details, store.state.startItem)
            assertFalse(store.state.isLoading)
            assertTrue(store.state.refundRulesLoading)
            store.accept(StartScreenStore.Intent.OnRetryRefundRules); runCurrent()
            assertEquals(1, ruleLoads)
            pending.complete(Unit); runCurrent()
            assertTrue(store.state.refundRulesError)
            assertNull(store.state.refundRules)
            assertNull(store.state.error)

            result = Result.success(custom)
            store.accept(StartScreenStore.Intent.OnRetryRefundRules); runCurrent()
            assertEquals(custom, store.state.refundRules)
            assertFalse(store.state.refundRulesError)
            assertEquals(1, detailsLoads)

            result = Result.success(custom.copy(source = StartRefundRules.Source.Global))
            store.accept(StartScreenStore.Intent.OnClickRetry); runCurrent()
            assertEquals(StartRefundRules.Source.Global, store.state.refundRules?.source)
            assertEquals(2, detailsLoads)
            assertEquals(3, ruleLoads)

            val lastPolicy = store.state.refundRules
            result = Result.failure(IllegalStateException("offline"))
            store.accept(StartScreenStore.Intent.OnRetryRefundRules); runCurrent()
            assertEquals(lastPolicy, store.state.refundRules)
            assertTrue(store.state.refundRulesError)
            assertFalse(store.state.refundRulesLoading)
            assertNull(store.state.error)

            result = Result.success(custom.copy(rules = emptyList()))
            store.accept(StartScreenStore.Intent.OnRetryRefundRules); runCurrent()
            assertTrue(store.state.refundRules!!.rules.isEmpty())
            assertFalse(store.state.refundRulesError)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }
}
