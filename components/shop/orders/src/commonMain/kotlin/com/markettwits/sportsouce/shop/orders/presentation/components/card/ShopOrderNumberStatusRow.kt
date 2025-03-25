package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderStatus

@Composable
internal fun ShopOrderNumberStatusRow(
    modifier: Modifier = Modifier,
    createAt : String,
    orderStatus: ShopOrderStatus
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = createAt,
            fontSize = 12.sp,
            fontFamily = FontNunito.semiBoldBold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        ShopOrderStatus(
            orderStatus = orderStatus
        )
    }
}