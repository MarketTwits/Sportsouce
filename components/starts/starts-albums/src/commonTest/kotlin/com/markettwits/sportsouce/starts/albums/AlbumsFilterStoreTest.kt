package com.markettwits.sportsouce.starts.albums

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.sportsouce.starts.albums.filter.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsFilterStoreTest {
    @Test fun yearIsDraftUntilApplyAndClosingDoesNotApplyReset() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val factory = AlbumsFilterStoreFactory(DefaultStoreFactory())
        val store = factory.create(2023)
        val labels = mutableListOf<AlbumsFilterStore.Label>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { labels += it } }
        try {
            store.accept(AlbumsFilterStore.Intent.SelectYear(2025))
            assertEquals(2025, store.state.year); assertTrue(labels.isEmpty())
            store.accept(AlbumsFilterStore.Intent.Reset)
            assertNull(store.state.year); assertTrue(labels.isEmpty())
            store.accept(AlbumsFilterStore.Intent.Dismiss)
            assertEquals(listOf<AlbumsFilterStore.Label>(AlbumsFilterStore.Label.Dismiss), labels)
            val reopened = factory.create(2023)
            try {
                assertEquals(2023, reopened.state.year)
                val applied = mutableListOf<AlbumsFilterStore.Label>()
                val applyJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { reopened.labels.collect { applied += it } }
                reopened.accept(AlbumsFilterStore.Intent.SelectYear(2025))
                reopened.accept(AlbumsFilterStore.Intent.Apply)
                assertEquals(listOf<AlbumsFilterStore.Label>(AlbumsFilterStore.Label.Apply(2025)), applied)
                reopened.accept(AlbumsFilterStore.Intent.Reset)
                reopened.accept(AlbumsFilterStore.Intent.Apply)
                assertEquals(AlbumsFilterStore.Label.Apply(null), applied.last())
                applyJob.cancel()
            } finally { reopened.dispose() }
        } finally { job.cancel(); store.dispose(); Dispatchers.resetMain() }
    }
}
