package com.markettwits.sportsouce.starts.starts.data

import com.arkivanov.decompose.value.MutableValue
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.log.LogTagProvider
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.starts.common.domain.*
import com.markettwits.sportsouce.starts.starts.domain.StartsRepository
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsTabUiState
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsUiState

internal class StartsRepositoryBase(
    private val service: SportSauceStartsApi,
    private val cache: StartsTabsCache,
    private val exceptionTracker: ExceptionTracker,
) : StartsRepository, LogTagProvider {

    override val tag: String = "StartsRepositoryBase"

    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)

    override suspend fun starts(forced: Boolean, page: Int) {
        val tab = StartsTab.fromPage(page)
        val cached = cache.get() ?: StartsTabsCacheModel()
        val currentState = ensureSuccessState(cached)

        if (!forced && currentState.tabs[tab.page].isInitialized) {
            starts.value = currentState
            return
        }

        starts.value = currentState.updateTab(
            tab = tab,
            isRefreshing = forced,
        ) { state ->
            state.copy(
                isLoading = state.items.isEmpty() || forced,
                isAppending = false,
                error = null,
                endReached = false,
                isInitialized = true,
            )
        }

        runCatching {
            loadPage(tab = tab, offset = 0)
        }.onSuccess { items ->
            val updatedCache = cached.withTabItems(tab, items)
            cache.set(value = updatedCache)
            starts.value = ensureSuccessState(updatedCache).updateTab(tab) { state ->
                state.copy(
                    items = items,
                    isLoading = false,
                    isAppending = false,
                    error = null,
                    endReached = items.size < STARTS_PAGE_SIZE,
                    isInitialized = true,
                )
            }.copy(isRefreshing = false)
        }.onFailure { error ->
            report(error)
            val fallbackItems = cached.itemsFor(tab)
            if (fallbackItems.isNotEmpty()) {
                starts.value = currentState.updateTab(tab) { state ->
                    state.copy(
                        items = fallbackItems,
                        isLoading = false,
                        isAppending = false,
                        error = null,
                        endReached = fallbackItems.size < STARTS_PAGE_SIZE,
                        isInitialized = true,
                    )
                }.copy(isRefreshing = false)
            } else if (currentState.tabs.any { it.items.isNotEmpty() }) {
                starts.value = currentState.updateTab(tab) { state ->
                    state.copy(
                        isLoading = false,
                        isAppending = false,
                        error = error.mapToSauceError(),
                        isInitialized = true,
                    )
                }.copy(isRefreshing = false)
            } else {
                starts.value = StartsUiState.Failed(error.mapToSauceError())
            }
        }
    }

    override suspend fun loadNext(page: Int) {
        val currentState = starts.value as? StartsUiState.Success ?: return
        val tab = StartsTab.fromPage(page)
        val tabState = currentState.tabs[tab.page]
        if (!tabState.isInitialized || tabState.isLoading || tabState.isAppending || tabState.endReached) {
            return
        }

        starts.value = currentState.updateTab(tab) { it.copy(isAppending = true, error = null) }

        runCatching {
            loadPage(tab = tab, offset = tabState.items.size)
        }.onSuccess { pageItems ->
            val merged = (tabState.items + pageItems).distinctBy(StartsListItem::id)
            val updatedCache = (cache.get() ?: StartsTabsCacheModel()).withTabItems(tab, merged)
            cache.set(value = updatedCache)
            starts.value = ensureSuccessState(updatedCache).updateTab(tab) {
                it.copy(
                    items = merged,
                    isLoading = false,
                    isAppending = false,
                    error = null,
                    endReached = pageItems.size < STARTS_PAGE_SIZE,
                    isInitialized = true,
                )
            }
        }.onFailure { error ->
            report(error)
            starts.value = currentState.updateTab(tab) {
                it.copy(
                    isAppending = false,
                    error = if (it.items.isEmpty()) error.mapToSauceError() else null,
                )
            }
        }
    }

    private suspend fun loadPage(
        tab: StartsTab,
        offset: Int,
    ): List<StartsListItem> = StartsPagingSource(
        startsApi = service,
        params = tab.params,
    ).load(offset = offset, limit = STARTS_PAGE_SIZE)

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
