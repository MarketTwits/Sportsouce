package com.markettwits.sportsouce.start.search.search.presentation.components.publish

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun StartsSearchBarPublic(
    modifier: Modifier = Modifier,
    onClickSearchPanel: () -> Unit,
    onClickSettings: () -> Unit,
) {
    val colors = SauceTheme.colors
    Row(
        modifier = modifier.padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            onClick = onClickSearchPanel,
            modifier = Modifier.weight(1f).heightIn(min = 52.dp),
            shape = SauceShapes.field,
            color = colors.surface,
            contentColor = colors.textMuted,
            border = BorderStroke(1.dp, colors.border),
        ) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(22.dp))
                Text("Поиск старта", style = MaterialTheme.typography.bodyLarge)
            }
        }
        IconButton(onClick = onClickSettings, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Default.Settings, contentDescription = "Настройки", tint = colors.textSecondary)
        }
    }
}
