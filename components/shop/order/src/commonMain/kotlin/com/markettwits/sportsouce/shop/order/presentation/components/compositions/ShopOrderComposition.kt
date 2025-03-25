package com.markettwits.sportsouce.shop.order.presentation.components.compositions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.shop.cart.domain.calculateTotalCost
import com.markettwits.sportsouce.shop.domain.mapper.formatPriceWithSpaces
import com.markettwits.sportsouce.shop.order.domain.model.ShopItemOrderResult
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderComposition(
    modifier: Modifier = Modifier,
    shopItems : List<ShopItemOrderResult>
) {
    ShopBasicSectorContent(modifier = modifier,title = "Состав заказа"){
        Column(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
            shopItems.forEach {
                ShopOrderCompositionItem(
                    modifier = Modifier.padding(4.dp),
                    itemCount = it.item.count,
                    imageUrl = it.item.item.visual.imageUrl.firstOrNull() ?: "",
                    totalPrice = formatPriceWithSpaces(it.item.calculateTotalCost()),
                    isSuccess = it.isAvailable,
                    message = it.message
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
    totalPrice : String,
    isSuccess : Boolean,
    message : String?
){
    Column(
        modifier = modifier,
    ) {
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
                .size(80.dp)
                .clip(Shapes.medium),
            model = imageUrl,
            contentDescription = null
        )
        if (isSuccess){
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "$totalPrice ₽",
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
        }else{
            message?.let { text ->
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = text,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}