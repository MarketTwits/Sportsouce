package com.markettwits.shop.catalog.presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.shop.catalog.presentation.component.CardsComponent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore


@Composable
fun ShopItemCardBaseList(
    modifier: Modifier = Modifier,
    component: CardsComponent,
) {

    val state by component.state.collectAsState()
    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant

    if (state.isLoading) {
        LoadingFullScreen()
    }
    if (state.isError) {
        FailedScreen(message = state.message, onClickRetry = {})
    }
    if (state.items.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            columns = GridCells.Fixed(2)
        ) {
            items(state.items) {
                ShopCard(shopItem = it, onItemClick = { id ->
                    component.obtainEvent(ShopCatalogStore.Intent.OnClickItem(id))
                })
            }
        }
    }


}


data class ShopItem(
    val id: String,
    val currentPrice: String,
    val previousPrice: String?,
    val discount: Int?,
    val title: String,
    val imageUrl: String,
)

@Composable
fun ShopCard(
    modifier: Modifier = Modifier,
    shopItem: ShopItem,
    onItemClick: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(height = 260.dp)
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
                modifier = Modifier.weight(0.6f),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
            )
            if (!previousPrice.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.weight(0.4f),
                    text = "$previousPrice ₽",
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    textDecoration = TextDecoration.LineThrough,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    fontFamily = FontNunito.medium(),
                )
                Text(
                    modifier = Modifier.weight(0.25f),
                    text = "-$discount%",
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
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
    }
}

private val items = listOf(
    ShopItem(
        "0",
        "4300 Р",
        null,
        null,
        "Супер пупер кросовки",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/September/06/04/89815f01-85d0-4e34-b975-961611b88e80.jpg?tr=w-400"

    ),
    ShopItem(
        "1",
        "12300 Р",
        null,
        null,
        "Кроссовки Hoka ROCKET X Шоссе 7(EGGSHELL BLUE / BLACK) (US 7, Бирюзовый)",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/September/02/12/5caab8f9-5183-4594-bdff-2a046b207534.jfif?tr=w-400"

    ),
    ShopItem(
        "0",
        "13300 Р",
        null,
        null,
        "befirst Multivitamin (60 шт, Жен, без вкуса)",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/June/07/05/c6a272f5-7f3c-4a8f-a73e-fa78de12dc75.jpeg?tr=w-400"

    ),
    ShopItem(
        "0",
        "43",
        "35",
        25,
        "Кроссовки ALTRA",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/June/07/05/c6a272f5-7f3c-4a8f-a73e-fa78de12dc75.jpeg?tr=w-400"
    ),
    ShopItem(
        "0",
        "4300 Р",
        null,
        null,
        "Кроссовки Hoka ROCKET X Шоссе 7(EGGSHELL BLUE / BLACK) (US 7, Бирюзовый)",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/September/06/04/89815f01-85d0-4e34-b975-961611b88e80.jpg?tr=w-400"

    ),
    ShopItem(
        "0",
        "4300 Р",
        null,
        null,
        "Супер пупер кросовки",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/September/06/04/89815f01-85d0-4e34-b975-961611b88e80.jpg?tr=w-400"

    ),
    ShopItem(
        "0",
        "4300 Р",
        "3900 Р",
        15,
        "Супер пупер кросовки",
        "https://ik.imagekit.io/5c6no6i1s/files/live/2024/March/27/04/90c6c5fb-d8ef-4b13-9a53-f7fab80a1192.webp?tr=w-400"

    ),

    )