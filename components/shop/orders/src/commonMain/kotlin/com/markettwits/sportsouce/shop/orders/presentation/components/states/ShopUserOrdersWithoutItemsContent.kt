package com.markettwits.sportsouce.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShopUserOrdersWithoutItemsContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = EMPTY_CART_IMAGE_URL,
                contentDescription = "empty orders"
            )
            Text(
                modifier = modifier.padding(4.dp),
                text = "Упс... У вас ещё нет заказов",
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold(),
                fontSize = 20.sp
            )
            Text(
                modifier = modifier.padding(4.dp),
                text = "Оформите заказ и он обязательно появится здесь, это просто !",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.regular(),
                fontSize = 16.sp
            )
        }
    }

}

private const val EMPTY_CART_IMAGE_URL = "https://cdn-icons-png.flaticon.com/512/10423/10423290.png"