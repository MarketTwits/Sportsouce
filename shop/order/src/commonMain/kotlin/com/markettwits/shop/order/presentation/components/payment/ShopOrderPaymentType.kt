package com.markettwits.shop.order.presentation.components.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopPaymentType

@Composable
internal fun ShopOrderPaymentType(
    modifier: Modifier = Modifier,
    shopFilterPaymentTypes : List<ShopPaymentType>,
    selectedShopDeliveryType: ShopDeliveryType,
    selectedPaymentType : ShopPaymentType,
    onClickChangePaymentType : (ShopPaymentType) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "Способ оплаты",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            shopFilterPaymentTypes.forEach { paymentType ->
                AnimatedVisibility(visible = paymentType.isAvailable(selectedShopDeliveryType)){
                    paymentType.mapToCard(
                        isSelected = paymentType == selectedPaymentType,
                        onClick = { onClickChangePaymentType(paymentType) }
                    )
                }
            }
        }
    }

}

@Composable
private fun PaymentTypeItem(
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
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
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
private fun ShopPaymentType.mapToCard(
    isSelected: Boolean,
    onClick: () -> Unit
){
    when(this){
        ShopPaymentType.Cache -> {
            PaymentTypeItem(
                title = "Наличными",
                icon = Icons.Default.Money,
                isSelected = isSelected,
                onClick = onClick
            )
        }
        ShopPaymentType.Online -> {
            PaymentTypeItem(
                title = "Онлайн",
                icon = Icons.Default.ShoppingCart,
                isSelected = isSelected,
                onClick = onClick
            )
        }
    }
}