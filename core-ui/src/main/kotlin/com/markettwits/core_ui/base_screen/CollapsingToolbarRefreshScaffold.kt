package com.markettwits.core_ui.base_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

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
        toolbarModifier = Modifier,
        toolbar = {
            toolbar()
        }
    ) {
        PullToRefreshScreen(
            isRefreshing = isRefreshing,
            onRefresh = {
                onRefresh()
            }) {
            body(it)
        }
    }
}