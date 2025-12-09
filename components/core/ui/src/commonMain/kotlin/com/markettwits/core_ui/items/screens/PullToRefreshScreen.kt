package com.markettwits.core_ui.items.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshScreen(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (isUsingPullToRefreshAvailable) {
        val state = rememberPullToRefreshState()
        PullToRefreshBox(
            modifier = modifier,
            isRefreshing = isRefreshing,
            state = state,
            onRefresh = onRefresh,
            indicator = {
                Indicator(
                    state = state,
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primary,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            },
            content = {
                content()
            }
        )
    } else {
        content()
    }
}

expect val isUsingPullToRefreshAvailable: Boolean