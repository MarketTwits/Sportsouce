package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions

@Composable
internal fun ShopItemExtraOptions(modifier: Modifier = Modifier, extraOption: List<ShopExtraOptions>, onClickOption: (String) -> Unit) {
    Column(modifier.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        extraOption.forEach { option ->
            Text(option.title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            FlowRow(Modifier.fillMaxWidth().selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                option.items.forEach { (id, value, selected) ->
                    FilterChipBase(selected = selected, label = value, onClick = { if (!selected) onClickOption(id) })
                }
            }
        }
    }
}
