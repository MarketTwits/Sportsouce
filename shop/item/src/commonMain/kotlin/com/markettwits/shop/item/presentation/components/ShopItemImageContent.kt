package com.markettwits.shop.item.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.FullImageContent
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito

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
    onClickImage: (String) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageUrl.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .heightIn(max = 400.dp)
    ) {
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
                model = imageUrl[index],
                contentDescription = "",
                contentScale = ContentScale.Fit,
                error = {
                    SubcomposeAsyncImageContent(
//                     painter = DefaultImages.EmptyImageStart()
                    )
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = modifier)
                }
            )
        }
        if (imageUrl.isNotEmpty()) {
            ShopItemImagePageIndicator(
                currentPageIndex = pagerState.targetPage,
                pageCount = pagerState.pageCount,
            )
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
            text = "${currentPageIndex + 1} / ${pageCount}",
            color = MaterialTheme.colorScheme.onTertiary,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
        )
    }
}