package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import com.markettwits.core_ui.items.theme.SauceTheme


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo

@Composable
internal fun ShopFilterSelectedParams(
    modifier: Modifier = Modifier,
    selectedCategoriesPath: List<ShopCategoryItem>,
    selectedOptions: List<ShopOptionInfo.Value>,
    onClickCategory: (ShopCategoryItem) -> Unit,
    onClickOptionValue: (ShopOptionInfo.Value) -> Unit,
) {
    val selectedCategory =
        if (selectedCategoriesPath.isNotEmpty()) {
            selectedCategoriesPath.filter { it.children.isNotEmpty() }
            selectedCategoriesPath.last()
        } else {
            null
        }

    val isShow = selectedCategoriesPath.isNotEmpty() || selectedOptions.isNotEmpty()

    if (isShow) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (selectedCategory != null) {
                ShopCategoryItem(
                    title = selectedCategory.title,
                    onClick = { onClickCategory(selectedCategory) }
                )
            }
            selectedOptions.forEach { option ->
                ShopCategoryItem(
                    title = option.name,
                    onClick = { onClickOptionValue(option) }
                )
            }
        }
    }

}

@Composable
private fun ShopCategoryItem(modifier: Modifier = Modifier, title: String, onClick: () -> Unit) {
    androidx.compose.material3.InputChip(selected = true, onClick = onClick,
        modifier = modifier.padding(horizontal = 4.dp).heightIn(min = 48.dp),
        label = { Text(title, style = MaterialTheme.typography.labelLarge) },
        trailingIcon = { Icon(Icons.Default.Close, "Снять фильтр $title", Modifier.size(18.dp)) },
        shape = com.markettwits.core_ui.items.theme.SauceShapes.badge,
        colors = androidx.compose.material3.InputChipDefaults.inputChipColors(
            selectedContainerColor = SauceTheme.colors.actionSoft, selectedLabelColor = SauceTheme.colors.onActionSoft,
            selectedTrailingIconColor = SauceTheme.colors.onActionSoft))
}
