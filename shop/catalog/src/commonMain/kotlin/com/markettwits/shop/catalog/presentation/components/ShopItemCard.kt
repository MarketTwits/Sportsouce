package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.markettwits.shop.catalog.domain.models.ShopItem


@Composable
fun ShopItemCard(
    modifier: Modifier = Modifier,
    shopItem: ShopItem,
    onItemClick: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(240.dp)
            .padding(4.dp)
            .clip(Shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                onItemClick(shopItem.id)
            }
    ) {
        Column {
            ImageCard(
                modifier = Modifier
                    .clip(Shapes.large)
                    .padding(2.dp)
                    .align(Alignment.CenterHorizontally)
                    .weight(0.6f),
                image = shopItem.imageUrl,
            )
            ShowCardPrice(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.Start)
                    .weight(0.4f),
                currentPrice = shopItem.currentPrice,
                previousPrice = shopItem.previousPrice,
                discount = shopItem.discount,
                title = shopItem.title
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
            .fillMaxSize()
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
            error = {
                if (image.isEmpty())
                //Handle empty image
//                        SubcomposeAsyncImageContent(
//                            modifier = modifier,
//                            painter = DefaultImages.EmptyImageStart()
//                        )
                else
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
            },
            loading = {
                Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
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
    discount: Int?,
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
        if (!previousPrice.isNullOrEmpty()) {
            Text(
                text = "-$discount%",
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontFamily = FontNunito.medium(),
            )
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
    }
}