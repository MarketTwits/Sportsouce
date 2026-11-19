package com.markettwits.core_ui.items.components.topbar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.bottomNavigationOverlap

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
    val dockOverlap = bottomNavigationOverlap()
    var hasFooter by remember { mutableStateOf(false) }
    var hasFab by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(Modifier.sauceTopBarGlass(glass)) {
                CompositionLocalProvider(LocalInsideGlassTopBar provides true) { topBar() }
            }
        },
        bottomBar = {
            DockSafeSlot(dockOverlap, { hasFooter = it }, bottomBar)
        },
        snackbarHost = {
            DockSafeSlot(if (hasFooter || hasFab) 0.dp else dockOverlap, content = snackbarHost)
        },
        floatingActionButton = {
            val overlap = if (hasFooter && floatingActionButtonPosition != FabPosition.EndOverlay) 0.dp else dockOverlap
            DockSafeSlot(overlap, { hasFab = it }, floatingActionButton)
        },
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { padding ->
        CompositionLocalProvider(LocalSauceTopBar provides glass) { content(padding) }
    }
}

/** Empty slots leave the viewport full height. Nonempty slots reserve space under their controls. */
@Composable
private fun DockSafeSlot(overlap: Dp, onPresent: ((Boolean) -> Unit)? = null, content: @Composable () -> Unit) {
    if (overlap == 0.dp && onPresent == null) {
        content()
        return
    }
    Box(
        Modifier.then(if (overlap > 0.dp) Modifier.layout { measurable, constraints ->
            val inset = overlap.roundToPx().coerceAtMost(constraints.maxHeight)
            val controls = measurable.measure(constraints.copy(maxHeight = constraints.maxHeight - inset))
            val height = if (controls.height > 0) controls.height + inset else 0
            layout(controls.width, height) { controls.placeRelative(0, 0) }
        } else Modifier).then(if (onPresent != null) Modifier.onSizeChanged { onPresent(it.height > 0) } else Modifier),
    ) { content() }
}
