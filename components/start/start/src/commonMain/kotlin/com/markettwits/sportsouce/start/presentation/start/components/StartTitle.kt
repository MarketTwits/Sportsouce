package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun StartTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        modifier = modifier.fillMaxWidth().semantics { heading() },
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        color = SauceTheme.colors.textPrimary,
    )
}
