package com.markettwits.starts.starts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.refresh.PullToRefreshScreen
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start_search.search.presentation.components.publish.StartsSearchBarPublic
import com.markettwits.starts.starts.presentation.component.MockStartsScreenComponent
import com.markettwits.starts.starts.presentation.component.StartsScreen
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.presentation.StartsScreenContent
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@Preview
@Composable
private fun StartsScreenPreview() {
    SportSouceTheme {
        StartsScreen(MockStartsScreenComponent())
    }
}

@Composable
fun StartsScreen(component: StartsScreen) {
    val starts by component.starts.subscribeAsState()
    var loading by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        when (starts) {
            is StartsUiState.Success -> {
                //TODO
                loading = false
                CollapsingToolbarScaffold(
                    modifier = Modifier,
                    scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
                    state = rememberCollapsingToolbarScaffoldState(),
                    toolbar = {
                        StartsSearchBarPublic(modifier = Modifier.clickable {
                            component.onSearchClick()
                        })
                    }
                ) {
                    PullToRefreshScreen(
                        isRefreshing = loading,
                        onRefresh = {
                            component.retry()
                            loading = true
                        }) {
                        Column(modifier = it) {
                            TabBar(content = { page ->
                                val items = (starts as StartsUiState.Success).items[page]
                                StartsScreenContent(
                                    items = items,
                                    onClick = component::onItemClick
                                )
                            })
                        }
                    }

                }
            }

            is StartsUiState.Failed -> {
                FailedScreen(
                    message = (starts as StartsUiState.Failed).message,
                    onClickRetry = {
                        component.retry()
                    },
                    onClickHelp = {})
            }

            is StartsUiState.Loading -> {
                LoadingScreen()
            }
        }
    }
}