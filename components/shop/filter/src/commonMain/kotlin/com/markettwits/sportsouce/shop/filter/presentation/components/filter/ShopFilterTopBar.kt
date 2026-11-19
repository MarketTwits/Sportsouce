package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ShopFilterTopBar(modifier: Modifier = Modifier, onClickBack: () -> Unit) {
    Row(modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text("Фильтры", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
        IconButton(onClickBack, Modifier.size(48.dp)) {
            Icon(Icons.Default.Close, "Закрыть фильтры", tint = SauceTheme.colors.textSecondary)
        }
    }
}
