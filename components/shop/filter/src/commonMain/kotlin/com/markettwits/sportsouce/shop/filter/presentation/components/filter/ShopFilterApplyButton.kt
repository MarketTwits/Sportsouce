package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant

@Composable
internal fun ShopFilterApplyButton(modifier: Modifier = Modifier, onClickReset: () -> Unit, onClickApply: () -> Unit) {
    BoxWithConstraints(modifier.fillMaxWidth().padding(16.dp)) {
        if (maxWidth < 300.dp * LocalDensity.current.fontScale) Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SauceButton("Применить", onClickApply)
            SauceButton("Сбросить", onClickReset, variant = SauceButtonVariant.Outlined)
        } else Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SauceButton("Сбросить", onClickReset, Modifier.weight(1f), variant = SauceButtonVariant.Outlined)
            SauceButton("Применить", onClickApply, Modifier.weight(1f))
        }
    }
}
