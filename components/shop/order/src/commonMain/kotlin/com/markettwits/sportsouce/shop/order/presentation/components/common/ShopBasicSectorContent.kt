package com.markettwits.sportsouce.shop.order.presentation.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ShopBasicSectorContent(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
    OnBackgroundCard(modifier) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            content()
        }
    }
}
