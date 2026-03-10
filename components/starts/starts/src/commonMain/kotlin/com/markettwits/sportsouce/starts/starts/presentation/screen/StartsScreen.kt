package com.markettwits.sportsouce.starts.starts.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.start.search.search.presentation.components.publish.StartsSearchBarPublic
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsScreen
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsUiState
import com.markettwits.sportsouce.starts.starts.presentation.components.StartsScreenList
import com.markettwits.sportsouce.starts.starts.presentation.components.TabBar


@Composable
fun StartsScreen(
    component: StartsScreen
) {
    val state by component.starts.subscribeAsState()
    var currentPage by remember { mutableIntStateOf(1) }

    CollapsingToolbarScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            Column {
                StartsSearchBarPublic(
                    onClickSearchPanel = component::onSearchClick,
                    onClickSettings = component::onSettingsClick
                )
            }
        }
    ) {
        PullToRefreshScreen(
            isRefreshing = (state as? StartsUiState.Success)?.isRefreshing == true,
            onRefresh = {
                component.retry(currentPage)
            }) {
            Column {
                TabBar(
                    onPageChanged = {
                        currentPage = it
                        component.onPageSelected(it)
                    },
                    content = { page ->
                        val tabState = (state as? StartsUiState.Success)?.tabs?.getOrNull(page)
                        StartsScreenList(
                            page = page,
                            state = tabState ?: when (state) {
                                is StartsUiState.Failed -> com.markettwits.sportsouce.starts.starts.presentation.component.StartsTabUiState(
                                    error = (state as StartsUiState.Failed).error,
                                    isInitialized = true,
                                )

                                is StartsUiState.Loading -> com.markettwits.sportsouce.starts.starts.presentation.component.StartsTabUiState(
                                    isLoading = true
                                )

                                is StartsUiState.Success -> error("Unexpected tab state")
                            },
                            onClickRetry = { component.retry(page) },
                            onClickItem = component::onItemClick,
                            onLoadNext = { component.onLoadNext(page) }
                        )
                    })
            }
        }
    }
}
