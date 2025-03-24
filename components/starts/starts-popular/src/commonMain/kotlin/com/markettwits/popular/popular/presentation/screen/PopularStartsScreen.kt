package com.markettwits.popular.popular.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.popular.popular.presentation.component.StartsPopularComponent
import com.markettwits.popular.popular.presentation.components.StartsPopularContent
import com.markettwits.popular.popular.presentation.store.StartsPopularStore

@Composable
internal fun PopularStartsScreen(component: StartsPopularComponent) {

    val state by component.state.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBarWithClip(title = "Популярное") {
                component.obtainEvent(StartsPopularStore.Intent.OnClickBack)
            }
        }
    ) {
        PullToRefreshScreen(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            isRefreshing = state.isLoading && state.starts.isNotEmpty(),
            onRefresh = {
                component.obtainEvent(StartsPopularStore.Intent.Retry)
            }) {
            if (state.starts.isNotEmpty()) {
                Column {
                    StartsPopularContent(
                        modifier = it,
                        items = state.starts
                    ) {
                        component.obtainEvent(StartsPopularStore.Intent.OnClickStart(it))
                    }
                }
            }
            if (state.isError) {
                FailedScreen(
                    message = state.message,
                    onClickRetry = {
                        component.obtainEvent(StartsPopularStore.Intent.Retry)
                    }
                )
            }
            if (state.isLoading) {
                LoadingFullScreen()
            }
        }
    }
}