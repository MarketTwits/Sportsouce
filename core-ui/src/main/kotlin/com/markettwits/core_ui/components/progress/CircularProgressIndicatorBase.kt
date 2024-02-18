package com.markettwits.core_ui.components.progress

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun CircularProgressIndicatorBase(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.tertiary,
        strokeCap = StrokeCap.Round,
        modifier = modifier
    )
}