package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Foundation lazy layouts with the same state, keys, scrolling and content DSLs. */
@Composable
fun SauceLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: LazyListScope.() -> Unit,
) {
    val viewport = rememberNavigationScrollViewport()
    LazyColumn(modifier.then(viewport.modifier), state, contentPadding.withNavigationPadding(viewport.bottomPadding, viewport.topPadding),
        reverseLayout, verticalArrangement, horizontalAlignment, flingBehavior, userScrollEnabled, overscrollEffect, content)
}

@Composable
fun SauceLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: LazyGridScope.() -> Unit,
) {
    val viewport = rememberNavigationScrollViewport()
    LazyVerticalGrid(columns, modifier.then(viewport.modifier), state, contentPadding.withNavigationPadding(viewport.bottomPadding, viewport.topPadding),
        reverseLayout, verticalArrangement, horizontalArrangement, flingBehavior, userScrollEnabled, overscrollEffect, content)
}

@Composable
fun SauceLazyVerticalStaggeredGrid(
    columns: StaggeredGridCells,
    modifier: Modifier = Modifier,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalItemSpacing: Dp = 0.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: LazyStaggeredGridScope.() -> Unit,
) {
    val viewport = rememberNavigationScrollViewport()
    LazyVerticalStaggeredGrid(columns, modifier.then(viewport.modifier), state, contentPadding.withNavigationPadding(viewport.bottomPadding, viewport.topPadding),
        reverseLayout, verticalItemSpacing, horizontalArrangement, flingBehavior, userScrollEnabled, overscrollEffect, content)
}
