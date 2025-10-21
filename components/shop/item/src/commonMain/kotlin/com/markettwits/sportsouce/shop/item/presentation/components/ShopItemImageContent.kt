package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
internal fun ShopItemImageContent(
    modifier: Modifier = Modifier,
    imageUrl: List<String>,
) {
    var isFullScreen: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedImageUrl: String by remember {
        mutableStateOf("")
    }

    FullImageContent(
        modifier = modifier,
        imageUrl = imageUrl,
        onClickImage = {
            selectedImageUrl = it
            isFullScreen = !isFullScreen
        }
    )
    AnimatedVisibility(
        visible = isFullScreen,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        FullImageScreen(
            image = imageUrl,
            selectedImageUrl = selectedImageUrl,
            onDismiss = { isFullScreen = !isFullScreen }
        )
    }
}

@Composable
private fun FullImageContent(
    modifier: Modifier = Modifier,
    imageUrl: List<String>,
    onClickImage: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .heightIn(max = 400.dp)
    ) {
        if (imageUrl.isNotEmpty()) {
            val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageUrl.size })

            HorizontalPager(
                modifier = Modifier.align(Alignment.Center),
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically,
            ) { index ->
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .background(Color.White)
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .clip(Shapes.large)
                        .noRippleClickable {
                            onClickImage(imageUrl[index])
                        },
                    model = imageRequestCrossfade(imageUrl[index]),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.outlineVariant)
                                .shimmer()
                        )
                    },
                    error = {
                        SubcomposeAsyncImageContent()
                    },
                    success = {
                        SubcomposeAsyncImageContent(modifier = modifier)
                    }
                )
            }
            ShopItemImagePageIndicator(
                currentPageIndex = pagerState.targetPage,
                pageCount = pagerState.pageCount,
            )
        } else {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clip(Shapes.large),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "No image",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Изображение отсутствует",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.ShopItemImagePageIndicator(
    modifier: Modifier = Modifier,
    currentPageIndex: Int,
    pageCount: Int,
) {
    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .padding(10.dp)
            .align(Alignment.BottomStart)
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = "${currentPageIndex + 1} / $pageCount",
            color = SportSouceColor.SportSouceLightBlueForDarkTheme,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
        )
    }
}
