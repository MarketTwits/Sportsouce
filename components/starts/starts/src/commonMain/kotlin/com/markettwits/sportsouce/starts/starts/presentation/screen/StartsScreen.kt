package com.markettwits.sportsouce.starts.starts.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    CollapsingToolbarScaffold(
        modifier = Modifier,
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
            isRefreshing = loading,
            onRefresh = {
                component.retry()
                loading = true
            }) {
            Column {
                TabBar(
                    content = { page ->
                        StartsScreenList(
                            modifier = it,
                            state = state,
                            page = page,
                            onClickRetry = component::retry,
                            onClickItem = component::onItemClick
                        )
                    })
            }
        }
    }
    LaunchedEffect(state) {
        if (state is StartsUiState.Failed || state is StartsUiState.Success)
            loading = false
    }
}

