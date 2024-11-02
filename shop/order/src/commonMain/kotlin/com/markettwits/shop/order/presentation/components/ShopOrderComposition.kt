package com.markettwits.shop.order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.domain.calculateTotalCost
import com.markettwits.shop.cart.domain.formatPrice

@Composable
internal fun ShopOrderComposition(
    modifier: Modifier = Modifier,
    shopItems : List<ShopItemCart>
) {
    ShopBasicSectorContent(modifier = modifier,title = "Состав заказа"){
        Row(modifier = Modifier.padding(10.dp)) {
            shopItems.forEach {
                ShopOrderCompositionItem(
                    modifier = Modifier.padding(4.dp),
                    itemCount = it.count,
                    imageUrl = it.item.visual.imageUrl.firstOrNull() ?: "",
                    totalPrice = it.calculateTotalCost().formatPrice()
                )
            }
        }
    }
}

@Composable
private fun ShopOrderCompositionItem(
    modifier: Modifier = Modifier,
    itemCount : Int,
    imageUrl : String,
    totalPrice : String
){
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "$itemCount товар(а)",
            fontSize = 12.sp,
            fontFamily = FontNunito.medium(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.outline
        )
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(Shapes.medium),
            model = imageUrl,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "$totalPrice ₽",
            fontSize = 12.sp,
            fontFamily = FontNunito.medium(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.outline
        )
    }
}