package com.markettwits.core_ui.items.components.progress

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun CircularProgressIndicatorBase(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.tertiary
) {
    CircularProgressIndicator(
        color = color,
        strokeCap = StrokeCap.Round,
        modifier = modifier
    )
}