package com.markettwits.sportsouce.shop.order.presentation.components.price

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderPriceContent(
    modifier: Modifier = Modifier,
    itemsCount: String,
    discount: String,
    totalCost: String
) {
    if (totalCost != "0"){
        ShopBasicSectorContent(modifier = modifier, title = "Ваш заказ") {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = "Товаров : ($itemsCount)",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.LightGray
                )
                DiscountRow(title = "Скидка :", value = discount)
                TotalRow(totalCost = totalCost)
            }
        }
    }
}

@Composable
private fun DiscountRow(modifier: Modifier = Modifier, title: String, value: String) {
    AnimatedVisibility(visible = value.isNotEmpty() && value != "0") {
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray
            )
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = " - $value ₽",
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceLightRed
            )
        }
    }
}

@Composable
private fun TotalRow(
    modifier: Modifier = Modifier,
    totalCost: String,
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Итого : ",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        AnimatedContent(
            targetState = totalCost,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                } else {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            }
        ) { value ->
            Text(
                text = "$value ₽",
                fontSize = 20.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}