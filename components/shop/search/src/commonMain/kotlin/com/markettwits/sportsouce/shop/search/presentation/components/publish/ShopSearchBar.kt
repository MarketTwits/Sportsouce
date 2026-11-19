package com.markettwits.sportsouce.shop.search.presentation.components.publish

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.*

@Composable
fun ShopSearchBar(modifier: Modifier = Modifier, query: String, isShowFilter: Boolean, onClickBack: () -> Unit,
    onClickSearchPanel: () -> Unit, onClickFilter: () -> Unit, onBrushClicked: () -> Unit) {
    Row(modifier.windowInsetsPadding(WindowInsets.statusBars).fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        IconButton(onClickBack, Modifier.size(SauceSizes.touchTarget)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = SauceTheme.colors.textSecondary)
        }
        Surface(onClick = onClickSearchPanel, modifier = Modifier.weight(1f).heightIn(min = SauceSizes.fieldMinHeight),
            shape = SauceShapes.button, color = SauceTheme.colors.surface, border = BorderStroke(1.dp, SauceTheme.colors.border)) {
            Row(Modifier.padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Search, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
                Text(query.ifBlank { "Поиск товара" }, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium,
                    color = if (query.isBlank()) SauceTheme.colors.textMuted else SauceTheme.colors.textPrimary,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
        if (query.isNotBlank()) IconButton(onBrushClicked, Modifier.size(SauceSizes.touchTarget)) {
            Icon(Icons.Default.Close, "Очистить поиск", tint = SauceTheme.colors.textSecondary)
        }
        if (isShowFilter) IconButton(onClickFilter, Modifier.size(SauceSizes.touchTarget)) {
            Icon(Icons.Default.Tune, "Фильтры", tint = SauceTheme.colors.textSecondary)
        }
    }
}
