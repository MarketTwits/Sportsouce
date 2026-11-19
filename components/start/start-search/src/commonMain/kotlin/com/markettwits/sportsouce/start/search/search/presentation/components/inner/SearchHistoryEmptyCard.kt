package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun SearchHistoryEmptyCard(modifier: Modifier = Modifier) {
    val colors = SauceTheme.colors
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(56.dp).background(colors.surfaceSubtle, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Search, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(28.dp))
        }
        Text("История поиска пуста", style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary, textAlign = TextAlign.Center)
        Text("Начните вводить название старта. Ваши поиски появятся здесь.",
            style = MaterialTheme.typography.bodyMedium, color = colors.textMuted, textAlign = TextAlign.Center)
    }
}
