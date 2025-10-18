package com.markettwits.core_ui.items.components.cards

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun OnBackgroundCard(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.medium,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.(Modifier) -> Unit,
) {
    val elevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp,
        pressedElevation = if (onClick != null) 6.dp else 4.dp,
        focusedElevation = 4.dp,
        hoveredElevation = if (onClick != null) 5.dp else 4.dp,
        draggedElevation = 8.dp,
        disabledElevation = 0.dp
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = colors,
        elevation = elevation,
        onClick = onClick ?: {}
    ) {
        content(modifier)
    }
}