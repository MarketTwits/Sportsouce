package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
fun ShopItemCardCompact(
    modifier: Modifier = Modifier,
    shopItem: ShopItem,
    onItemClick: (ShopItem) -> Unit,
    cardWidth: Dp = 140.dp,
    cardHeight: Dp = 230.dp,
    showBorder: Boolean = true,
    showImageBorder: Boolean = false,
    imageAspectRatio: Float? = null,
    imageBottomSpacing: Dp = 8.dp,
) {
    Box(
        modifier = modifier
            .padding(6.dp)
            .clip(Shapes.large)
            .width(cardWidth)
            .height(cardHeight)
            .background(MaterialTheme.colorScheme.background)
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                        shape = Shapes.large
                    )
                } else {
                    Modifier
                }
            )
            .clickable {
                onItemClick(shopItem)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ImageCard(
                modifier = Modifier
                    .clip(Shapes.large)
                    .align(Alignment.CenterHorizontally)
                    .then(
                        if (imageAspectRatio != null) {
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(imageAspectRatio)
                        } else {
                            Modifier.weight(0.68f)
                        }
                    ),
                image = shopItem.visual.imageUrl,
                showBorder = showImageBorder,
            )
            Spacer(modifier = Modifier.height(imageBottomSpacing))
            ShowCardPrice(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
                    .then(
                        if (imageAspectRatio != null) {
                            Modifier
                        } else {
                            Modifier.weight(0.32f)
                        }
                    ),
                currentPrice = shopItem.price.currentPrice,
                previousPrice = shopItem.price.previousPrice,
                discount = shopItem.price.discount,
                title = shopItem.visual.displayName
            )
            if (imageAspectRatio != null) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: List<String>,
    showBorder: Boolean,
) {
    Box(modifier = modifier) {
        if (image.isNotEmpty()) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (showBorder) {
                            Modifier.border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.16f),
                                shape = Shapes.large
                            )
                        } else {
                            Modifier
                        }
                    ),
                model = imageRequestCrossfade(image.first()),
                filterQuality = FilterQuality.High,
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                clipToBounds = false,
                loading = {
                    Box(
                        modifier = modifier
                            .shimmer(
                                tiltAngle = 30,
                                gradientColors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                                    Color.Transparent,
                                )
                            ).background(MaterialTheme.colorScheme.primaryContainer)
                    )
                },
                success = {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier
                            .background(Color.White, RectangleShape)
                    )
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (showBorder) {
                            Modifier.border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.16f),
                                shape = Shapes.large
                            )
                        } else {
                            Modifier
                        }
                    )
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    imageVector = Icons.Default.Photo,
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = ""
                )
            }
        }
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
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$currentPrice ₽",
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
            )
            if (!previousPrice.isNullOrEmpty()) {
                Text(
                    text = "$previousPrice₽",
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    textDecoration = TextDecoration.LineThrough,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.regular(),
                )
            }
            if (!previousPrice.isNullOrEmpty() && discount != null) {
                Text(
                    text = "-$discount%",
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    fontFamily = FontNunito.medium(),
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 14.sp,
            softWrap = true,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
        )
    }
}
