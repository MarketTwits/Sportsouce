package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo

@Composable
internal fun ShopFilterOptions(options: List<ShopOptionInfo>, selectedOptions: List<ShopOptionInfo.Value>, onOptionClick: (ShopOptionInfo.Value) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        options.forEach { option ->
            Text(option.name, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                option.values.forEach { value ->
                    FilterChipBase(selected = selectedOptions.any { it.uuid == value.uuid }, label = value.name,
                        onClick = { onOptionClick(value) })
                }
            }
        }
    }
}
