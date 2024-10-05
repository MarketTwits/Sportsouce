package com.markettwits.shop.item.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
    var isFullScreen: Boolean by remember {
        mutableStateOf(false)
    }
    var selectedImageUrl : String by remember {
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
        FullImageScreen(image = selectedImageUrl, onDismiss = { isFullScreen = !isFullScreen })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FullImageContent(
    modifier: Modifier = Modifier,
    imageUrl: List<String>,
    isPortrait: Boolean = true,
    onClickImage: (String) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageUrl.size })

    val boxModifier = if (isPortrait) modifier
        .noRippleClickable {
            onClickImage(imageUrl[pagerState.currentPage])
        }
        .fillMaxWidth()
        .height(300.dp) else {
        modifier.noRippleClickable {
            onClickImage(imageUrl[pagerState.currentPage])
        }
    }
    val imageModifier =
        if (isPortrait) Modifier
            .wrapContentSize()
            .width(200.dp)
            .clip(RoundedCornerShape(10.dp))
        else Modifier.fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
    Box(
        modifier = boxModifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (imageUrl.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(Shapes.medium)
                    .padding(10.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "${pagerState.targetPage + 1} / ${pagerState.pageCount}",
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                )
            }
        }
        HorizontalPager(
            state = pagerState
        ) { index ->
            Box {
                if (isPortrait) {
                    SubcomposeAsyncImage(
                        model = imageUrl[index],
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.2f),
                        error = {
                            //
                        },
                        success = {
                            SubcomposeAsyncImageContent(modifier = modifier)
                        }
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(18.dp)
                        .align(Alignment.Center),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = imageUrl[index],
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier,
                        error = {
                            //
                        },
                        success = {
                            SubcomposeAsyncImageContent(modifier = modifier)
                        }
                    )
                }
            }
        }
    }
}