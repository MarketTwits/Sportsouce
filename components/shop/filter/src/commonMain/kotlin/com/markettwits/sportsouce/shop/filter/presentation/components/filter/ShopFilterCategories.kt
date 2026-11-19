package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem

@Composable
internal fun ShopFilterCategories(modifier: Modifier = Modifier, categories: List<ShopCategoryItem>,
    currentPath: List<ShopCategoryItem>, onClickCategory: (ShopCategoryItem?) -> Unit) {
    val current = currentPath.lastOrNull()
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(current?.title ?: "Все категории", style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
        if (currentPath.isNotEmpty()) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SauceButton("Все категории", { onClickCategory(null) }, variant = SauceButtonVariant.Text, fillWidth = false)
            currentPath.forEach { category -> SauceButton(category.title, { onClickCategory(category) },
                variant = SauceButtonVariant.Text, fillWidth = false) }
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            (current?.children ?: categories).forEach { category ->
                SauceButton(category.title, { onClickCategory(category) }, variant = SauceButtonVariant.Outlined, fillWidth = false)
            }
        }
    }
}
