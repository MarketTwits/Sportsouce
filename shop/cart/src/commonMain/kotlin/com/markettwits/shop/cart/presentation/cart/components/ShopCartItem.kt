package com.markettwits.shop.cart.presentation.cart.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.cart.domain.ShopItemCart

@Composable
internal fun ShopCartItem(
    modifier: Modifier = Modifier,
    shopCartItemCart: ShopItemCart,
    onClickIncrease: (ShopItemCart) -> Unit,
    onClickDecrease: (ShopItemCart) -> Unit,
    onItemClick: (ShopItemCart) -> Unit,
) {
    Row(modifier = modifier
        .padding(4.dp)
        .clip(Shapes.large)
        .background(MaterialTheme.colorScheme.primary)
        .clickable {
            onItemClick(shopCartItemCart)
        }) {
        ImageCard(
            modifier = Modifier
                .padding(2.dp),
            image = shopCartItemCart.imageUrl.firstOrNull() ?: "",
        )
        Column {
            ShowCardPrice(
                modifier = Modifier
                    .padding(4.dp),
                currentPrice = shopCartItemCart.currentPrice,
                previousPrice = shopCartItemCart.previousPrice,
                quantity = shopCartItemCart.quantity,
                title = shopCartItemCart.title
            )
            SubscriptionCounter(
                monthOfCount = shopCartItemCart.count,
                onClickDecrease = { onClickDecrease(shopCartItemCart) },
                onClickIncrease = { onClickIncrease(shopCartItemCart) }
            )
        }

    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: String,
) {
    Box(
        modifier = modifier
            .size(140.dp)
            .background(Color.White)
    ) {
        SubcomposeAsyncImage(
            model = imageRequestCrossfade(image),
            filterQuality = FilterQuality.High,
            contentDescription = "",
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            clipToBounds = true,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
            },
            error = {
                Box(modifier = Modifier
                    .fillMaxSize()){
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Photo,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = ""
                    )
                }
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
    }
}


@Composable
private fun ShowCardPrice(
    modifier: Modifier,
    currentPrice: String,
    previousPrice: String?,
    quantity : Int,
    title: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$currentPrice ₽",
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            if (!previousPrice.isNullOrEmpty()) {
                Text(
                    text = "$previousPrice₽",
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    textDecoration = TextDecoration.LineThrough,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    fontFamily = FontNunito.medium(),
                )
            }
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 14.sp,
            softWrap = true,
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
            fontFamily = FontNunito.semiBoldBold(),
        )
        if (quantity < 10){
            Text(
                text = "Осталось всего $quantity товаров на складе",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp,
                fontFamily = FontNunito.medium(),
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SubscriptionCounter(
    modifier: Modifier = Modifier,
    monthOfCount: Int,
    onClickIncrease: () -> Unit,
    onClickDecrease: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                .clickable(onClick = onClickDecrease)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
                    .animateContentSize(),
                text = "–",
                fontSize = 25.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        AnimatedContent(
            targetState = monthOfCount,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                } else {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            }
        ) { count ->
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                text = count.toString(),
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Box(
            modifier = Modifier
                .clickable(onClick = onClickIncrease)
                .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd =8.dp))
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
                    .animateContentSize(),
                text = "+",
                fontSize = 25.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}