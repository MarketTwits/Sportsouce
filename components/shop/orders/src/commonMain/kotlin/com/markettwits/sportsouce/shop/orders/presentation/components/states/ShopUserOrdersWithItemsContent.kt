package com.markettwits.sportsouce.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder
import com.markettwits.sportsouce.shop.orders.presentation.components.card.ShopUserOrderCard

@Composable
internal fun ShopUserOrdersWithItemsContent(
    modifier: Modifier = Modifier,
    items : List<ShopUserOrder>
) {
    Column(modifier = modifier) {
        if(items.isNotEmpty()){
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                text = "Всего ${items.size} заказов",
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
            items.forEach {
                ShopUserOrderCard(
                    modifier = Modifier.padding(10.dp),
                    order = it
                )
            }
        }
    }
}