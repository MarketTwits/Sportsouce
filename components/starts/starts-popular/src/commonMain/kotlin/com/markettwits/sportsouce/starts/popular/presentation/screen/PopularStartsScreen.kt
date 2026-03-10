package com.markettwits.sportsouce.starts.popular.presentation.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.starts.popular.presentation.component.StartsPopularComponent
import com.markettwits.sportsouce.starts.popular.presentation.components.StartsPopularContent
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore

@Composable
internal fun PopularStartsScreen(component: StartsPopularComponent) {

    val state by component.state.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithClip(title = "Популярное") {
                component.obtainEvent(StartsPopularStore.Intent.OnClickBack)
            }
        }
    ) { paddingValues ->
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
        if (state.starts.isNotEmpty()) {
            StartsPopularContent(
                isRefreshing = state.isLoading && state.starts.isNotEmpty(),
                items = state.starts,
                onRefresh = {
                    component.obtainEvent(StartsPopularStore.Intent.Retry)
                },
                onClick = {
                    component.obtainEvent(StartsPopularStore.Intent.OnClickStart(it))
                }
            )
        }
        state.error?.let {
            it.SauceErrorScreen(
                modifier = Modifier.fillMaxSize(),
                onClickRetry = { component.obtainEvent(StartsPopularStore.Intent.Retry) },
            )
        }
        if (state.isLoading) {
            LoadingFullScreen()
        }
    }
}