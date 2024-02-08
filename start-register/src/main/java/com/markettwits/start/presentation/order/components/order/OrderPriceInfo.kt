package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun OrderPriceInfo(
    modifier: Modifier = Modifier,
    membersCount: Int,
    discountInCache: Double,
    total: Double,
) {
    Text(
        modifier = modifier,
        text = "Ваш заказ",
        fontSize = 18.sp,
        fontFamily = FontNunito.bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Color.Black
    )
    Text(
        modifier = modifier,
        text = "Участников : $membersCount",
        fontSize = 14.sp,
        fontFamily = FontNunito.bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Color.LightGray
    )
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            modifier = modifier,
            text = "Скидка : ",
            fontSize = 14.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.LightGray
        )
        Text(
            modifier = modifier,
            text = " - $discountInCache ₽",
            fontSize = 14.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceLightRed
        )
    }
    Text(
        modifier = modifier,
        text = "Итого : $total ₽",
        fontSize = 18.sp,
        fontFamily = FontNunito.bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Color.Black
    )
}