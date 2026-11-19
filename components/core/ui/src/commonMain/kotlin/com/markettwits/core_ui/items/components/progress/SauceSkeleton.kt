package com.markettwits.core_ui.items.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

/** Decorative loading placeholder; the section supplies its single accessible loading label. */
@Composable
fun SauceSkeleton(modifier: Modifier = Modifier, shape: Shape = SauceShapes.badge) {
    Box(modifier.clearAndSetSemantics {}.clip(shape).shimmer()
        .background(SauceTheme.colors.textMuted.copy(alpha = 0.12f)))
}
