package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopPageItem

@Composable
internal fun ShopItemPriceRow(modifier: Modifier = Modifier, price: ShopItem.Price) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${price.currentPrice} ₽",
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            if (!price.previousPrice.isNullOrEmpty()) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Text(
                    text = "${price.previousPrice} ₽",
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.light(),
                )
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Text(
                    text = "- ${price.discount}%",
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.light(),
                )
            }
        }
    }
}