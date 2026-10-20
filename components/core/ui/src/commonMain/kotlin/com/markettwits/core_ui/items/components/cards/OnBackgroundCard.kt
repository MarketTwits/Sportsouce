package com.markettwits.core_ui.items.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun OnBackgroundCard(
    modifier: Modifier = Modifier,
    shape: Shape = SauceShapes.card,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = SauceTheme.colors.surface,
        contentColor = SauceTheme.colors.textPrimary,
    ),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.(Modifier) -> Unit,
) {
    val elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    val border = BorderStroke(1.dp, SauceTheme.colors.border)
    if (onClick != null) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            border = border,
            elevation = elevation,
            onClick = onClick,
        ) { content(Modifier) }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            border = border,
            elevation = elevation,
        ) { content(Modifier) }
    }
}
