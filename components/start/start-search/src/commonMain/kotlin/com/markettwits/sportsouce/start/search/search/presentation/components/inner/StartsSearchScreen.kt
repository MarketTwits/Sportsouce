package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.start.search.search.presentation.component.StartsSearchComponent
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore

@Composable
fun StartsSearchScreen(component: StartsSearchComponent) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            StartsSearchBarInner(
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
            AnimatedVisibility(
                visible = state.filter.items.find { it.selected.isNotEmpty() }?.selected?.isNotEmpty()
                    ?: false,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            val screenState = when {
                state.isLoading -> ScreenState.LOADING
                state.isError -> ScreenState.ERROR
                state.query.isEmpty() && state.filter.filterIsEmpty() -> ScreenState.HISTORY
                else -> ScreenState.RESULTS
            }
            AnimatedContent(
                targetState = screenState,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) togetherWith fadeOut(
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + slideOutVertically(
                        targetOffsetY = { -it / 4 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                },
                label = "screen_state_animation"
            ) { currentState ->
                when (currentState) {
                    ScreenState.LOADING -> {
                        LoadingFullScreen()
                    }

                    ScreenState.ERROR -> {
                        FailedScreen(message = state.message) {
                            component.obtainEvent(StartsSearchStore.Intent.OnClickRetry)
                        }
                    }

                    ScreenState.HISTORY -> {
                        if (state.searchHistory.isNotEmpty()) {
                            SearchHistoryColumn(
                                items = state.searchHistory,
                                onClick = {
                                    component.obtainEvent(StartsSearchStore.Intent.OnClickHistoryItem(it))
                                },
                                onDelete = {
                                    component.obtainEvent(StartsSearchStore.Intent.OnDeleteHistoryItem(it))
                                }
                            )
                        } else {
                            SearchHistoryEmptyCard()
                        }
                    }

                    ScreenState.RESULTS -> {
                        SearchResultColumn(
                            starts = state.starts,
                            onClickStart = { startItem, startTitle ->
                                component.obtainEvent(
                                    StartsSearchStore.Intent.OnClickStart(startItem)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

private enum class ScreenState {
    LOADING,
    ERROR,
    HISTORY,
    RESULTS
}