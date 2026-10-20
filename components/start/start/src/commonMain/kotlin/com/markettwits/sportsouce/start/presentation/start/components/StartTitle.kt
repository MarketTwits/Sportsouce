package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun StartTitle(modifier: Modifier = Modifier, title: String) {
    Text(title, modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge,
        color = SauceTheme.colors.textPrimary)
}
