package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.theme.Shapes

@Composable
internal fun ShopOrderImage(
    modifier: Modifier = Modifier,
    imageUrl : String
){
    AsyncImage(
        modifier = modifier
            .clip(Shapes.medium)
            .size(60.dp),
        model = imageUrl,
        contentDescription = ""
    )
}