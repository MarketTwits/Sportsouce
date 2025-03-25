package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.shop.order.domain.model.ShopOrderStatus

@Composable
internal fun ShopOrderStatus(
    modifier: Modifier = Modifier,
    orderStatus: ShopOrderStatus
){

    val color by remember {
        mutableStateOf(orderStatus.mapToColor())
    }

    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = orderStatus.toString(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = color
        )
    }
}

internal fun ShopOrderStatus.mapToColor() : Color =
    when(this){
        ShopOrderStatus.Cancelled -> SportSouceColor.SportSouceLightRed
        ShopOrderStatus.Completed -> SportSouceColor.SportSouceRegistryOpenGreen
        ShopOrderStatus.Confirmed -> SportSouceColor.SportSouceStartEndedPink
        ShopOrderStatus.Empty -> Color.Gray
        ShopOrderStatus.Pending -> SportSouceColor.SportSouceRegistryCommingSoonYellow
    }