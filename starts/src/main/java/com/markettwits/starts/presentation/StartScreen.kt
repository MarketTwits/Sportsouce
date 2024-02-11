package com.markettwits.starts.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start_search.search.presentation.components.publish.StartsSearchBarPublic
import com.markettwits.starts.components.TabBar
import com.markettwits.starts_common.presentation.StartsScreenContent


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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        when (starts) {
            is StartsUiState.Success -> {
                Column {
                    StartsSearchBarPublic(modifier = Modifier.clickable {
                        component.onSearchClick()
                    })
                    TabBar(content = { page ->
                        val items = (starts as StartsUiState.Success).items[page]
                        StartsScreenContent(
                            items = items,
                            onClick = component::onItemClick
                        )
                    })
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

@Composable
fun StartsScreenSingle(component: StartsScreen) {
    val starts by component.starts.subscribeAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        when (starts) {
            is StartsUiState.Success -> {
                Column {
                    val items = (starts as StartsUiState.Success).items[0]
                    StartsScreenContent(
                        items = items,
                        onClick = component::onItemClick
                    )
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