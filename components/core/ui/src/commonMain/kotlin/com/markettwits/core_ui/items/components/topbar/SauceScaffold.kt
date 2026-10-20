package com.markettwits.core_ui.items.components.topbar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/** Material layout/IME/FAB behaviour, with one shared glass host for every screen toolbar. */
@Composable
fun SauceScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val glass = rememberSauceTopBarState()
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(Modifier.sauceTopBarGlass(glass)) {
                CompositionLocalProvider(LocalInsideGlassTopBar provides true) { topBar() }
            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { padding ->
        // Padding stays in the caller's safe slot. Shared scroll primitives grow *inside* that
        // slot and capture before ancestor clips, so pinned actions never move under the bar.
        CompositionLocalProvider(LocalSauceTopBar provides glass) { content(padding) }
    }
}
