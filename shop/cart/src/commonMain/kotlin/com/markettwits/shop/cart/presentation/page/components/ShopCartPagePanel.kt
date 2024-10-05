package com.markettwits.shop.cart.presentation.page.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
internal fun ShopCartItemEmpty(
    modifier: Modifier = Modifier,
    onClickAddToCart: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier
            .padding(10.dp)
            .height(55.dp)
            .fillMaxWidth(),
        onClick = onClickAddToCart,
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        Text(
            modifier = Modifier.padding(2.dp),
            text = "В корзину",
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
        )
    }
}

@Composable
internal fun ShopCartItemExpanded(
    modifier: Modifier = Modifier,
    countInCart: Int,
    quantity: Int,
    onClickCart: () -> Unit,
    onClickAddToCart: () -> Unit,
    onClickRemoveFromCart: () -> Unit
) {
    Column(modifier = modifier) {
        AnimatedVisibility(quantity <= countInCart){
            Box(modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(Shapes.medium)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
            ){
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "На складе больше нет такого товара",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                modifier = Modifier.weight(0.5f),
                onClick = onClickCart,
                containerColor = SportSouceColor.SportSouceRegistryOpenGreen
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "В корзине",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            SubscriptionCounter(
                modifier = Modifier.weight(0.5f),
                countInCart = countInCart,
                quantity = quantity,
                onClickDecrease = onClickRemoveFromCart,
                onClickIncrease = onClickAddToCart
            )
        }
    }
}

@Composable
private fun SubscriptionCounter(
    modifier: Modifier = Modifier,
    countInCart: Int,
    quantity: Int,
    onClickIncrease: () -> Unit,
    onClickDecrease: () -> Unit
) {

    Row(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                .clickable(onClick = onClickDecrease)
        ) {
            Text(
                modifier = Modifier
                    .offset(y = (-7).dp)
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .animateContentSize(),
                text = "–",
                fontSize = 38.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = countInCart.toString(),
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.tertiary
        )
        val addIsAvailable = quantity > countInCart
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .clickable(onClick = {
                    if (addIsAvailable) {
                        onClickIncrease()
                    }
                })
        ) {
            Text(
                modifier = Modifier
                    .offset(y = (-7).dp)
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .animateContentSize(),
                text = "+",
                fontSize = 38.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}