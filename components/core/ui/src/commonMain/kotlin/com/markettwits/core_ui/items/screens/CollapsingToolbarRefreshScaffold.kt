package com.markettwits.core_ui.items.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScope
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CollapsingToolbarRefreshScaffold(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    toolbar: @Composable() (CollapsingToolbarScope.() -> Unit),
    body: @Composable() ((Modifier) -> Unit)
) {
    CollapsingToolbarScaffold(
        modifier = modifier,
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            toolbar()
        }
    ) {
        PullToRefreshScreen(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ) {
            body(it)
        }
    }
}