package com.markettwits.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.shop.order.domain.model.getOrderDetailUrl
import com.markettwits.shop.orders.domain.models.ShopUserOrder

@Composable
internal fun ShopUserOrderCard(
    modifier: Modifier = Modifier,
    order : ShopUserOrder
) {
    val intent = rememberIntentActionByPlatform()

    OnBackgroundCard(
        modifier = modifier,
        onClick = {
            intent.openWebPage(order.order.getOrderDetailUrl())
        }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            ShopOrderNumberStatusRow(
                modifier = Modifier.fillMaxWidth(),
                createAt = order.order.createAt,
                orderStatus = order.order.status
            )
            Text(
                text =  "№ ${order.order.id}",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                order.items.forEach {
                    ShopOrderItemRow(
                        imageUrl = it.images.firstOrNull() ?: "",
                        title = it.name,
                        count = it.orderProduct.count.toString(),
                        totalPrice = it.price
                    )
                }
            }
            ShopOrderInfoRow(
                title = "Доставка",
                value = order.order.shippingMethod.toString()
            )
            ShopOrderInfoRow(
                title = "Оплата",
                value = order.order.paymentMethod.toString()
            )
            ShopOrderInfoRow(
                title = "Итого",
                value = "${order.order.cost} ₽"
            )
        }
    }
}


