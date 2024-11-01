package com.markettwits.shop.cart.presentation.cart.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
internal fun ShopCartOrder(
    modifier: Modifier = Modifier,
    itemsCount: String,
    discount: String,
    totalCost: String,
    isByCache: Boolean,
    isByDelivery: Boolean,
    isCreateOrderAvailable: Boolean,
    onClickChangePaymentType: () -> Unit,
    onClickChangeDeliveryWay : () -> Unit,
    onClickCreateOrder: () -> Unit,
) {
    Column(modifier = modifier) {
        PaymentTypeContent(
            isByCache = isByCache,
            isByDelivery = isByDelivery,
            onClickChangePaymentType = onClickChangePaymentType,
            onClickChangeDeliveryWay = onClickChangeDeliveryWay
        )
        OrderContent(
            itemsCount = itemsCount,
            discount = discount,
            totalCost = totalCost
        )
        CreateOrderButton(onClick = onClickCreateOrder, isAvailable = isCreateOrderAvailable)
    }
}

@Composable
private fun OrderContent(
    modifier: Modifier = Modifier,
    itemsCount: String,
    discount: String,
    totalCost: String
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "Ваш заказ",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
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
        Row(
            modifier = Modifier
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
}

@Composable
private fun DiscountRow(modifier: Modifier = Modifier, title: String, value: String) {
    AnimatedVisibility(visible = value.isNotEmpty()) {
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
private fun PaymentTypeContent(
    modifier: Modifier = Modifier,
    isByCache: Boolean,
    isByDelivery: Boolean,
    onClickChangePaymentType: () -> Unit,
    onClickChangeDeliveryWay : () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = modifier,
            text = "Способ оплаты",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isByDelivery) {
                PaymentTypeItem(
                    title = "Наличными",
                    icon = Icons.Default.Money,
                    isSelected = isByCache,
                    onClick = onClickChangePaymentType
                )
            }
            PaymentTypeItem(
                title = "Онлайн",
                icon = Icons.Default.ShoppingCart,
                isSelected = !isByCache,
                onClick = onClickChangePaymentType
            )
        }
        Text(
            modifier = modifier,
            text = "Способ доставка",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PaymentTypeItem(
                title = "Самовывоз",
                icon = Icons.Default.Money,
                isSelected = !isByDelivery,
                onClick = onClickChangeDeliveryWay
            )
            PaymentTypeItem(
                title = "Доставка",
                icon = Icons.Default.ShoppingCart,
                isSelected = isByDelivery,
                onClick = onClickChangeDeliveryWay
            )
        }
    }
}

@Composable
fun PaymentTypeItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .clip(Shapes.medium)
            .clickable {
                if (!isSelected)
                    onClick()
            },
        shape = Shapes.medium,
        border = if (isSelected) BorderStroke(
            3.dp, MaterialTheme.colorScheme.secondary
        ) else
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun CreateOrderButton(
    modifier: Modifier = Modifier,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(10.dp)
            .height(55.dp)
            .fillMaxWidth(),
        enabled = isAvailable,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
        shape = Shapes.medium,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp
        ),
        content = {
            Text(
                modifier = Modifier.padding(2.dp),
                text = "Оформить",
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
            )
        }
    )
}