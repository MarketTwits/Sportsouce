package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderStatus

@Composable
internal fun ShopOrderNumberStatusRow(modifier: Modifier = Modifier, createAt: String, orderStatus: ShopOrderStatus) {
    FlowRow(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(createAt, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
        ShopOrderStatus(orderStatus = orderStatus)
    }
}
