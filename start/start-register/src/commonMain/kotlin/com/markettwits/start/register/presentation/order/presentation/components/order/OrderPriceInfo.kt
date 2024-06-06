package com.markettwits.start.register.presentation.order.presentation.components.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun OrderPriceInfo(
    modifier: Modifier = Modifier,
    membersCount: Int,
    discountPromo: Int,
    discountAge: Int,
    discountCombo: Int,
    total: Int,
) {
    Text(
        modifier = modifier,
        text = "Ваш заказ",
        fontSize = 18.sp,
        fontFamily = FontNunito.bold(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.tertiary
    )
    Text(
        modifier = modifier,
        text = "Участников : $membersCount",
        fontSize = 14.sp,
        fontFamily = FontNunito.bold(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Color.LightGray
    )
    DiscountRow(title = "Скидка по возрасту :", value = discountAge)
    DiscountRow(title = "Скидка по промокоду :", value = discountPromo)
    DiscountRow(title = "Скидка за комбо старт :", value = discountCombo)
    Text(
        modifier = modifier,
        text = "Итого : $total ₽",
        fontSize = 18.sp,
        fontFamily = FontNunito.bold(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun DiscountRow(modifier: Modifier = Modifier, title: String, value: Int) {
    AnimatedVisibility(visible = value != 0) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray
            )
            Text(
                modifier = Modifier.padding(10.dp),
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