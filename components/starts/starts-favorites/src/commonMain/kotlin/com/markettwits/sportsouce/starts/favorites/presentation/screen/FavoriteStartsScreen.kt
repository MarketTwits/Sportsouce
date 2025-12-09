package com.markettwits.sportsouce.starts.favorites.presentation.screen

import androidx.compose.foundation.layout.Box
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
import com.markettwits.sportsouce.starts.favorites.presentation.component.StartsFavoritesComponent
import com.markettwits.sportsouce.starts.favorites.presentation.components.StartsFavoritesContent
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore

@Composable
fun FavoriteStartsScreen(component: StartsFavoritesComponent) {

    val state by component.state.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithClip(title = "Избранное") {
                component.obtainEvent(StartsPopularStore.Intent.OnClickBack)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            val visibleItems = state.starts.filterNot { it.id in state.removedStartIds }

            StartsFavoritesContent(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                isEmpty = visibleItems.isEmpty() && state.error == null,
                isLoading = state.isLoading,
                isRefreshing = state.isLoading && state.starts.isNotEmpty(),
                items = visibleItems,
                deletingStartIds = state.deletingStartIds,
                removedStartIds = state.removedStartIds,
                onRefresh = {
                    component.obtainEvent(StartsPopularStore.Intent.Retry)
                },
                onClick = {
                    component.obtainEvent(StartsPopularStore.Intent.OnClickStart(it))
                },
                onFavoriteClick = {
                    component.obtainEvent(StartsPopularStore.Intent.OnClickRemoveStart(it))
                },
                onAddToFavorites = {
                    component.obtainEvent(StartsPopularStore.Intent.OnClickAddStart(it))
                }
            )

            state.error?.let { error ->
                error.SauceErrorScreen(
                    onClickRetry = {
                        component.obtainEvent(StartsPopularStore.Intent.Retry)
                    }
                )
            }

            if (state.isLoading && state.starts.isEmpty()) {
                LoadingFullScreen()
            }
        }
    }
}