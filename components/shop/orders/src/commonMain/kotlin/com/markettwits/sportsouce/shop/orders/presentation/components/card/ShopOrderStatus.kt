package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderStatus

@Composable
internal fun ShopOrderStatus(modifier: Modifier = Modifier, orderStatus: ShopOrderStatus) {
    SauceStatusBadge(orderStatus.toString(), modifier, tone = when (orderStatus) {
        ShopOrderStatus.Cancelled -> SauceStatusTone.Danger
        ShopOrderStatus.Completed -> SauceStatusTone.Success
        ShopOrderStatus.Confirmed -> SauceStatusTone.Neutral
        ShopOrderStatus.Pending -> SauceStatusTone.Warning
        ShopOrderStatus.Empty -> SauceStatusTone.Neutral
    })
}
