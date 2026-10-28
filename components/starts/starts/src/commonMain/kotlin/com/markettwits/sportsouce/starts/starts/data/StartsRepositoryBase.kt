package com.markettwits.sportsouce.starts.starts.data

import com.arkivanov.decompose.value.MutableValue
import com.markettwits.cahce.Cache
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.log.LogTagProvider
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.starts.common.domain.*
import com.markettwits.sportsouce.starts.starts.domain.StartsRepository
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsTabUiState
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsUiState

internal class StartsRepositoryBase(
    private val service: SportSauceStartsApi,
    private val cache: Cache<StartsTabsCacheModel>,
    private val exceptionTracker: ExceptionTracker,
) : StartsRepository, LogTagProvider {

    override val tag: String = "StartsRepositoryBase"

    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)

    // Requests for different tabs may finish in any order. Serialize cache merges and
    // coalesce work for the same tab without blocking another tab's refresh.
    private val tabLocks = List(4) { Mutex() }
    private val cacheLock = Mutex()
    private val refreshingTabs = mutableSetOf<Int>()

    override suspend fun starts(forced: Boolean, page: Int) {
        val tab = StartsTab.fromPage(page)
        val lock = tabLocks[tab.page]
        if (!lock.tryLock()) {
            if (!forced) return
            val current = starts.value as? StartsUiState.Success
            if (current?.tabs?.get(tab.page)?.isLoading == true) {
                // A manual pull can join the existing fresh request and show its indicator.
                refreshingTabs.add(tab.page)
                starts.value = current.copy(isRefreshing = true)
                return
            }
            // Let an append finish before a manual refresh replaces its loaded window.
            lock.lock()
        }
        try {
            val cached = cache.get() ?: StartsTabsCacheModel()
            val currentState = ensureSuccessState(cached)
            if (forced) refreshingTabs.add(tab.page)
            starts.value = currentState.updateTab(tab) { state ->
                state.copy(
                    isLoading = true,
                    error = null,
                    isInitialized = true,
                )
            }.copy(isRefreshing = refreshingTabs.isNotEmpty())

            // Revalidate the loaded window so returning to a scrolled list does not
            // shrink it back to the first page and move the user's scroll position.
            val limit = maxOf(STARTS_PAGE_SIZE, currentState.tabs[tab.page].items.size)
            val items = loadPage(tab = tab, offset = 0, limit = limit)
            val updatedCache = saveTab(tab, items)
            starts.value = ensureSuccessState(updatedCache)
            updateTab(tab) {
                it.copy(
                    items = items,
                    error = null,
                    endReached = items.size < limit,
                    isInitialized = true,
                )
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            report(error)
            val current = starts.value as? StartsUiState.Success
            if (current != null && current.tabs.any { it.items.isNotEmpty() }) {
                updateTab(tab) {
                    it.copy(error = if (it.items.isEmpty()) error.mapToSauceError() else null)
                }
            } else {
                starts.value = StartsUiState.Failed(error.mapToSauceError())
            }
        } finally {
            refreshingTabs.remove(tab.page)
            updateTab(tab) { it.copy(isLoading = false, isAppending = false) }
            lock.unlock()
        }
    }

    override suspend fun loadNext(page: Int) {
        val tab = StartsTab.fromPage(page)
        val lock = tabLocks[tab.page]
        if (!lock.tryLock()) return
        try {
            val currentState = starts.value as? StartsUiState.Success ?: return
            val tabState = currentState.tabs[tab.page]
            if (!tabState.isInitialized || tabState.isLoading || tabState.endReached) return
            updateTab(tab) { it.copy(isAppending = true, error = null) }
            val pageItems = loadPage(tab = tab, offset = tabState.items.size)
            val merged = (tabState.items + pageItems).distinctBy(StartsListItem::id)
            val updatedCache = saveTab(tab, merged)
            starts.value = ensureSuccessState(updatedCache)
            updateTab(tab) {
                it.copy(
                    items = merged,
                    error = null,
                    endReached = pageItems.size < STARTS_PAGE_SIZE,
                    isInitialized = true,
                )
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            report(error)
            updateTab(tab) {
                it.copy(error = if (it.items.isEmpty()) error.mapToSauceError() else null)
            }
        } finally {
            updateTab(tab) { it.copy(isAppending = false) }
            lock.unlock()
        }
    }

    private suspend fun saveTab(tab: StartsTab, items: List<StartsListItem>): StartsTabsCacheModel =
        cacheLock.withLock {
            val updated = (cache.get() ?: StartsTabsCacheModel()).withTabItems(tab, items)
            cache.set(value = updated)
            updated
        }

    private fun updateTab(tab: StartsTab, transform: (StartsTabUiState) -> StartsTabUiState) {
        val current = starts.value as? StartsUiState.Success ?: return
        starts.value = current.updateTab(tab, transform = transform)
            .copy(isRefreshing = refreshingTabs.isNotEmpty())
    }

    private suspend fun loadPage(
        tab: StartsTab,
        offset: Int,
        limit: Int = STARTS_PAGE_SIZE,
    ): List<StartsListItem> = StartsPagingSource(
        startsApi = service,
        params = tab.params,
    ).load(offset = offset, limit = limit)

    private fun ensureSuccessState(cacheModel: StartsTabsCacheModel): StartsUiState.Success {
        val currentState = starts.value as? StartsUiState.Success
        if (currentState != null) {
            return currentState
        }
        return StartsUiState.Success(
            tabs = StartsTab.entries.map { tab ->
                val items = cacheModel.itemsFor(tab)
                StartsTabUiState(
                    items = items,
                    isLoading = false,
                    isAppending = false,
                    error = null,
                    endReached = items.isNotEmpty() && items.size < STARTS_PAGE_SIZE,
                    isInitialized = items.isNotEmpty(),
                )
            }
        )
    }

    private fun StartsUiState.Success.updateTab(
        tab: StartsTab,
        isRefreshing: Boolean = this.isRefreshing,
        transform: (StartsTabUiState) -> StartsTabUiState,
    ): StartsUiState.Success {
        val updatedTabs = tabs.toMutableList()
        updatedTabs[tab.page] = transform(updatedTabs[tab.page])
        return copy(tabs = updatedTabs, isRefreshing = isRefreshing)
    }

    private fun StartsTabsCacheModel.itemsFor(tab: StartsTab): List<StartsListItem> = when (tab) {
        StartsTab.MAIN -> main
        StartsTab.ACTUAL -> actual
        StartsTab.PAST -> past
        StartsTab.PREVIEW -> preview
    }

    private fun StartsTabsCacheModel.withTabItems(
        tab: StartsTab,
        items: List<StartsListItem>,
    ): StartsTabsCacheModel = when (tab) {
        StartsTab.MAIN -> copy(main = items)
        StartsTab.ACTUAL -> copy(actual = items)
        StartsTab.PAST -> copy(past = items)
        StartsTab.PREVIEW -> copy(preview = items)
    }

    private fun report(error: Throwable) {
        exceptionTracker.reportException(error)
    }

    private enum class StartsTab(
        val page: Int,
        val params: StartsPagingParams,
    ) {
        MAIN(
            page = 0,
            params = StartsPagingParams(
                isMain = true,
                openFirst = true,
            )
        ),
        ACTUAL(
            page = 1,
            params = StartsPagingParams(
                isGroup = true,
                statuses = listOf(StartStatus.REGISTRATION_OPEN, StartStatus.ANNOUNCEMENT),
            )
        ),
        PAST(
            page = 2,
            params = StartsPagingParams(
                isGroup = true,
                statuses = listOf(StartStatus.ENDED),
            )
        ),
        PREVIEW(
            page = 3,
            params = StartsPagingParams(
                isGroup = true,
                statuses = listOf(StartStatus.ANNOUNCEMENT),
            )
        );

        companion object {
            fun fromPage(page: Int): StartsTab = entries.firstOrNull { it.page == page } ?: ACTUAL
        }
    }
}
