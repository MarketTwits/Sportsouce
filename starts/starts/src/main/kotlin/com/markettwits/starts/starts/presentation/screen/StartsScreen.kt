package com.markettwits.starts.starts.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.base_screen.PullToRefreshScreen
import com.markettwits.start_search.search.presentation.components.publish.StartsSearchBarPublic
import com.markettwits.starts.starts.presentation.component.StartsScreen
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts.starts.presentation.components.StartsScreenList
import com.markettwits.starts.starts.presentation.components.TabBar
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartsScreen(
    component: StartsScreen,
    notification: @Composable ((Modifier) -> Unit),
    showNotification: Boolean
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
                StartsSearchBarPublic(modifier = Modifier.clickable {
                    component.onSearchClick()
                })
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
                            notification = notification,
                            showNotification = showNotification,
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

