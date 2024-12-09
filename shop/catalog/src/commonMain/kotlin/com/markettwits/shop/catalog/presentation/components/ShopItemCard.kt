package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.shop.domain.model.ShopItem


@Composable
fun ShopItemCard(
    modifier: Modifier = Modifier,
    shopItem: ShopItem,
    onItemClick: (ShopItem) -> Unit,
) {
    Box(
        modifier = modifier
            .width(140.dp)
            .height(260.dp)
            .padding(4.dp)
            .clip(Shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                onItemClick(shopItem)
            }
    ) {
        Column {
            ImageCard(
                modifier = Modifier
                    .clip(Shapes.large)
                    .padding(2.dp)
                    .align(Alignment.CenterHorizontally)
                    .weight(0.7f),
                image = shopItem.visual.imageUrl,
            )
            ShowCardPrice(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.Start)
                    .weight(0.3f),
                currentPrice = shopItem.price.currentPrice,
                previousPrice = shopItem.price.previousPrice,
                discount = shopItem.price.discount,
                title = shopItem.visual.displayName
            )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: List<String>,
) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { image.size })
        HorizontalPager(
            modifier = Modifier
                .weight(0.9f)
                .clip(Shapes.large)
                .padding(2.dp)
                .fillMaxSize(),
            state = pagerState
        ) { index ->
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = imageRequestCrossfade(image[index]),
                filterQuality = FilterQuality.High,
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                clipToBounds = true,
                loading = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = Modifier)
                }
            )
        }
        if (image.size > 1){
            ShopImagePageIndicator(
                Modifier.weight(0.1f),
                pagerState = pagerState
            )
        }
        if (image.isEmpty()){
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
        }
    }
}

@Composable
private fun ShopImagePageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState
) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration)
                    MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(4.dp)
            )
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$currentPrice ₽",
                color = SportSouceColor.SportSouceLighBlue,
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
                color = SportSouceColor.SportSouceLighBlue,
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