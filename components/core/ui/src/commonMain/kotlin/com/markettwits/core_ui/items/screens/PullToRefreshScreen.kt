package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun PullToRefreshScreen(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
//    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
//        onRefresh()
//    })
//    PullRefreshLayout(
//        modifier = Modifier,
//        state = state,
//        flipped = false,
//        indicator = {
//            PullRefreshIndicator(
//                state = state,
//                flipped = false,
//                backgroundColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.tertiary
//            )
//        }
//    ) {
    Column {
        content(modifier)
    }

    //   }

}