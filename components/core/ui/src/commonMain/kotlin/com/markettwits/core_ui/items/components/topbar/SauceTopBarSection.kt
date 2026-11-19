package com.markettwits.core_ui.items.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import com.markettwits.core_ui.items.theme.SauceTheme

/**
 * A pinned sibling below the toolbar: tabs, categories or selected filters.
 * It shares the screen's backdrop and material; its controls never become a blur source.
 * Group adjacent rows in one section. Nested sections reuse the surrounding material.
 */
@Composable
fun SauceTopBarSection(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    content: @Composable ColumnScope.() -> Unit,
) {
    val host = LocalSauceTopBar.current
    val nested = LocalInsideGlassTopBar.current
    val material = when {
        nested -> Modifier
        host != null -> Modifier.sauceTopBarGlass(host, primary = false)
        else -> Modifier.background(SauceTheme.colors.surface)
    }
    val density = LocalDensity.current
    val safeTop = windowInsets.getTop(density)
    var topInWindow by remember { mutableFloatStateOf(Float.POSITIVE_INFINITY) }
    // A collapsing toolbar can move its body to the window edge. Keep controls below the
    // system icons while the material itself continues all the way behind the status bar.
    val inset = if (host != null && !nested && safeTop > 0) {
        Modifier.onGloballyPositioned { topInWindow = it.positionInWindow().y }
            .padding(top = with(density) { (safeTop - topInWindow).coerceAtLeast(0f).toDp() })
    } else Modifier
    Column(modifier.fillMaxWidth().then(material).then(inset)) {
        CompositionLocalProvider(LocalInsideGlassTopBar provides true) { content() }
    }
}
