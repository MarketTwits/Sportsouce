package com.markettwits.core_ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun OnBackgroundCard(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.medium,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    content: @Composable ColumnScope.(Modifier) -> Unit,
) {
    Card(
        modifier = modifier
            .shadow(1.dp, shape = shape)
            .fillMaxWidth(),
        shape = shape,
        colors = colors
    ) {
        content(modifier)
    }
}