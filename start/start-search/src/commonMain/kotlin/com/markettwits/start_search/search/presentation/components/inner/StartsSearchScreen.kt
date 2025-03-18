package com.markettwits.start_search.search.presentation.components.inner

import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.start_search.search.presentation.component.StartsSearchComponent
import com.markettwits.start_search.search.presentation.store.StartsSearchStore

@Composable
fun StartsSearchScreen(component: StartsSearchComponent) {
    val state by component.model.collectAsState()
    Scaffold(
        modifier = Modifier,
        topBar = {
            StartsSearchBarInner(
                modifier = Modifier,
                query = state.query,
                isWithFilter = state.filter.filterIsEmpty(),
                onQueryChanged = {
                    component.obtainEvent(StartsSearchStore.Intent.ChangeTextFiled(it))
                },
                onBrushClicked = {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickBrushText)
                },
                onFilterClicked = {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickFilter)
                },
                onClickBack = {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickBack)
                },
            )
        },
        bottomBar = {
            androidx.compose.animation.AnimatedVisibility(
                visible = state.filter.items.find { it.selected.isNotEmpty() }?.selected?.isNotEmpty()
                    ?: false,
                enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top),
                exit = slideOutVertically() + shrinkVertically()
            ) {
                SearchFilterContent(
                    filterParams = state.filter.selectedValueToString(),
                    onClickPanel = {
                        component.obtainEvent(StartsSearchStore.Intent.OnClickFilter)
                    },
                    onClickRemoveFilter = {
                        component.obtainEvent(StartsSearchStore.Intent.OnClickRemoveFilter)
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ).background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                LoadingFullScreen()
            }
            if (state.isError) {
                FailedScreen(message = state.message) {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickRetry)
                }
            }
            if (state.query.isEmpty() && state.filter.filterIsEmpty()) {
                SearchHistoryColumn(
                    items = state.searchHistory
                ) {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickHistoryItem(it))
                }
            } else {
                SearchResultColumn(
                    starts = state.starts,
                    onClickStart = { startId, startTitle ->
                        component.obtainEvent(
                            StartsSearchStore.Intent.OnClickStart(startId, startTitle)
                        )
                    }
                )
            }
        }
    }
}