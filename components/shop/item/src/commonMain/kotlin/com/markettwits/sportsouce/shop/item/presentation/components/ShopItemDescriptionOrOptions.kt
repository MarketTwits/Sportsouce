package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
internal fun ShopItemDescriptionOrOptions(modifier: Modifier = Modifier, description: String, options: List<ShopItem.Option>) {
    var selected by remember { mutableStateOf(0) }
    Column(modifier.padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SauceSegmentedTabs(listOf("Описание", "Характеристики"), selected, { selected = it })
        if (selected == 0) HtmlText(text = description, color = SauceTheme.colors.textPrimary)
        else Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(option.optionTitle, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                    Text(option.optionValue, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary)
                }
            }
        }
    }
}
