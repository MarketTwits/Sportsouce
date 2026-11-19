package com.markettwits.sportsouce.start

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.domain.StartRefundRules
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.domain.mapper.StartsListItemToStartItemMapper
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory
import com.markettwits.sportsouce.start.presentation.series.StartRelatedKind
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.*
import kotlinx.serialization.json.*
import java.lang.reflect.Proxy
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class StartArchiveStoreTest {
    private fun card(id: Int) = StartsListItem(id, "Страшно добрый забег", "", "25 октября 2026",
        StartsListItem.StatusCode(3, "Регистрация"), "", "", "", false, "4 км", views = 1)
    private val archive = listOf(card(29), card(113), card(318))
    private val details = StartsListItemToStartItemMapper.mapToPartialStartItem(card(318)).copy(
        startSeries = StartItem.StartSeries.Value(3, "Парковая серия", ""),
        startArchive = StartItem.StartArchive(1, "Страшно добрый забег", ""))
    private inline fun <reified T> unused(): T = Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
        error("Unexpected ${method.name}")
    } as T

    @Test fun seriesAndArchiveLoadIndependentlyAndArchiveCanRetryWithoutReloadingDetails() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        var loads = 0
        var archiveLoads = 0
        var failArchive = true
        val pending = CompletableDeferred<Unit>()
        val repository = object : StartRepository by unused<StartRepository>() {
            override suspend fun refundRules(startId: Int) = Result.success(
                StartRefundRules(startId, "", StartRefundRules.Source.Global, emptyList()))
            override suspend fun start(startId: String, relaunch: Boolean): Result<StartItem> { loads++; return Result.success(details) }
            override suspend fun startsRecommended(startId: String) = Result.success(emptyList<StartsListItem>())
            override suspend fun isStartInFavorite(startId: String) = Result.success(false)
            override suspend fun startsSeries(seriesId: Int): Result<List<StartsListItem>> { assertEquals(3, seriesId); return Result.success(listOf(card(300))) }
            override suspend fun startsArchive(archiveId: Int): Result<List<StartsListItem>> {
                assertEquals(1, archiveId); archiveLoads++; pending.await()
                return if (failArchive) Result.failure(IllegalStateException("offline")) else Result.success(archive)
            }
        }
        val store = StartScreenStoreFactory(DefaultStoreFactory(), repository, unused<ExceptionTracker>(), unused<IntentAction>())
            .create(StartScreenInput.Id(318))
        try {
            val labels = mutableListOf<StartScreenStore.Label>()
            store.labels.onEach { labels += it }.launchIn(backgroundScope)
            runCurrent()
            assertTrue(store.state.archiveLoading)
            assertFalse(store.state.isLoading)
            assertEquals(listOf(300), store.state.startsSeries.map { it.id })
            store.accept(StartScreenStore.Intent.OnRetryArchive); runCurrent()
            assertEquals(1, archiveLoads)
            pending.complete(Unit); runCurrent()
            assertTrue(store.state.archiveError); assertNull(store.state.error)
            assertFalse(store.state.archiveLoading)
            store.accept(StartScreenStore.Intent.OnClickArchive); runCurrent()
            assertTrue(labels.none { it is StartScreenStore.Label.OnClickArchive })
            failArchive = false
            store.accept(StartScreenStore.Intent.OnRetryArchive); runCurrent()
            assertEquals(2, archiveLoads); assertEquals(1, loads)
            assertFalse(store.state.archiveError)
            store.accept(StartScreenStore.Intent.OnClickArchive); runCurrent()
            val route = labels.filterIsInstance<StartScreenStore.Label.OnClickArchive>().single()
            assertEquals(318, route.startId); assertEquals(archive, route.starts)
            assertEquals(listOf(300), store.state.startsSeries.map { it.id })
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun startWithoutArchiveDoesNotRequestItOrExposeArchiveNavigation() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val repository = object : StartRepository by unused<StartRepository>() {
            override suspend fun refundRules(startId: Int) = Result.success(
                StartRefundRules(startId, "", StartRefundRules.Source.Global, emptyList()))
            override suspend fun start(startId: String, relaunch: Boolean) = Result.success(details.copy(
                startArchive = null, startSeries = StartItem.StartSeries.Empty))
            override suspend fun startsRecommended(startId: String) = Result.success(emptyList<StartsListItem>())
            override suspend fun isStartInFavorite(startId: String) = Result.success(false)
        }
        val store = StartScreenStoreFactory(DefaultStoreFactory(), repository, unused<ExceptionTracker>(), unused<IntentAction>())
            .create(StartScreenInput.Id(318))
        try {
            val labels = mutableListOf<StartScreenStore.Label>()
            store.labels.onEach { labels += it }.launchIn(backgroundScope)
            runCurrent()
            assertNull(store.state.startItem?.startArchive)
            assertFalse(store.state.archiveLoading); assertFalse(store.state.archiveError)
            assertTrue(store.state.startsArchive.isEmpty())
            store.accept(StartScreenStore.Intent.OnClickArchive)
            store.accept(StartScreenStore.Intent.OnRetryArchive); runCurrent()
            assertTrue(labels.none { it is StartScreenStore.Label.OnClickArchive })
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun savedSeriesRoutesRemainCompatibleAndArchiveRouteRetainsItsKind() {
        val json = Json { ignoreUnknownKeys = true }
        val old = """{"starts":[],"currentStartId":318}"""
        assertEquals(StartRelatedKind.Series, json.decodeFromString<RootStartScreenComponent.Config.RelatedStarts>(old).kind)
        val archiveRoute = RootStartScreenComponent.Config.RelatedStarts(archive, 318, StartRelatedKind.Archive)
        assertEquals(archiveRoute, json.decodeFromJsonElement<RootStartScreenComponent.Config.RelatedStarts>(json.encodeToJsonElement(archiveRoute)))
    }
}
