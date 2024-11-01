package com.markettwits.shop.order.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.shop.order.domain.model.ShopDeliveryType


@Composable
internal fun ShopOrderDeliveryType(
    modifier: Modifier = Modifier,
    shopDeliveryType: List<ShopDeliveryType>,
    selectedOption: ShopDeliveryType,
    onClickOption: (ShopDeliveryType) -> Unit
) {
    ShopBasicSectorContent(modifier = modifier,title = "Способ получения"){
        SelectorOptions(
            options = shopDeliveryType,
            selectedOption = selectedOption,
            onClickOption = {
                onClickOption(it)
            }
        )
    }
}

@Composable
private fun SelectorOptions(
    modifier: Modifier = Modifier,
    options : List<ShopDeliveryType>,
    selectedOption : ShopDeliveryType,
    onClickOption : (ShopDeliveryType) -> Unit
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .shadow(2.dp, shape = Shapes.medium),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row {
            options.forEach {
                SelectorOption(
                    modifier= Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                    text = it.mapToString(),
                    isSelected = selectedOption.mapToString() == it.mapToString(),
                    onClick = {
                        onClickOption(it)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectorOption(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline
    )
    val padding by animateDpAsState(targetValue = if (isSelected) 12.dp else 8.dp)

    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = padding, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp
        )
    }
}

internal fun ShopDeliveryType.mapToString() : String {
    return when(this){
        ShopDeliveryType.Delivery -> "Доставка"
        ShopDeliveryType.Pickup -> "Самовывоз"
    }
}
