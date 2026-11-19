package com.markettwits.sportsouce.starts.albums

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.sportsouce.starts.albums.domain.*
import com.markettwits.sportsouce.starts.albums.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class StartsAlbumsStoreTest {
    @Test fun searchDebouncesAndYearCancelsPendingSearchWhileOpeningUsesAlbumId() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = object : StartsAlbumsRepository {
            override suspend fun albums(offset: Int, limit: Int, filterText: String, year: Int?) = StartAlbumsPage(emptyList(), 0)
        }
        val store = StartsAlbumsStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            val initialFlow = store.state.items
            store.accept(StartsAlbumsStore.Intent.Search("Лыжи"))
            assertEquals("Лыжи", store.state.filterText)
            assertSame(initialFlow, store.state.items)
            advanceTimeBy(200)
            store.accept(StartsAlbumsStore.Intent.Search("Трейл"))
            advanceTimeBy(349); assertSame(initialFlow, store.state.items)
            advanceTimeBy(1); runCurrent(); assertNotSame(initialFlow, store.state.items)
            store.accept(StartsAlbumsStore.Intent.Search("Бег"))
            store.accept(StartsAlbumsStore.Intent.SelectYear(2025))
            val yearFlow = store.state.items
            assertEquals(2025, store.state.year)
            advanceTimeBy(500); runCurrent(); assertSame(yearFlow, store.state.items)
            val labels = mutableListOf<StartsAlbumsStore.Label>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { labels += it } }
            store.accept(StartsAlbumsStore.Intent.OpenAlbum(StartAlbumsItem.Album(27, "Фото", 448, "")))
            store.accept(StartsAlbumsStore.Intent.OpenAlbum(StartAlbumsItem.Album(15, "Пусто", 0, "")))
            store.accept(StartsAlbumsStore.Intent.OpenFilters)
            store.accept(StartsAlbumsStore.Intent.Back)
            assertEquals(listOf(StartsAlbumsStore.Label.OpenAlbum(27), StartsAlbumsStore.Label.OpenFilters(2025), StartsAlbumsStore.Label.Back), labels)
            job.cancel()
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun corePagingKeepsEmptyAlbumGroupsWithinRawPageBoundaries() = runTest {
        val calls = mutableListOf<Pair<Int, Int>>()
        val repo = object : StartsAlbumsRepository {
            override suspend fun albums(offset: Int, limit: Int, filterText: String, year: Int?): StartAlbumsPage {
                calls += offset to limit
                assertEquals("Трейл", filterText); assertEquals(2026, year)
                return StartAlbumsPage(if (offset == 0) List(10) { StartAlbumsItem(it, "Пусто", "", emptyList()) }
                    else listOf(StartAlbumsItem(309, "Трейл", "", listOf(StartAlbumsItem.Album(27, "Фото", 448, "")))), 11)
            }
        }
        val source = StartsAlbumsPagingSource(repo, "Трейл", 2026, 10)
        assertEquals(10, source.load(0, 10).size)
        assertEquals(27, source.load(10, 10).single().albums.single().id)
        assertEquals(11, source.getTotalCount())
        assertEquals(listOf(0 to 10, 10 to 10), calls)
    }
}
