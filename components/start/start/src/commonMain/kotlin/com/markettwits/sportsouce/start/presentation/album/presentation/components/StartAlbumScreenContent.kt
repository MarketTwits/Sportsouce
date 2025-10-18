package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.screenWidthDp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun StartAlbumScreenContent(
    modifier: Modifier = Modifier,
    items: List<String>,

    ) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var loadedItemCount by rememberSaveable { mutableIntStateOf(20) }

    val loadedItems = remember(items, loadedItemCount) {
        items.take(loadedItemCount.coerceAtMost(items.size))
    }

    val scrollableState = rememberLazyGridState()
    val screenWidth = calculateWindowSizeClass().screenWidthDp

    val columnCount = rememberSaveable(screenWidth) {
        val minColumnWidth = 140.dp
        val calculatedColumns = (screenWidth / minColumnWidth).toInt()
        calculatedColumns.coerceIn(2, 5)
    }

    val showScrollToTopButton by remember {
        derivedStateOf {
            scrollableState.firstVisibleItemIndex > columnCount * 2
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                    animationSpec = tween(300),
                    initialScale = 0.8f
                ),
                exit = fadeOut(animationSpec = tween(300)) + scaleOut(
                    animationSpec = tween(300),
                    targetScale = 0.8f
                ),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            scrollableState.animateScrollToItem(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Прокрутить наверх"
                    )
                }
            }
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = scrollableState,
            columns = GridCells.Fixed(columnCount),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp),
            content = {
                items(
                    count = loadedItems.size,
                    key = { index -> loadedItems[index] }
                ) { index ->
                    ImageCardContent(
                        imageUrl = loadedItems[index],
                        onClick = { selectedIndex = index }
                    )
                }
                if (loadedItems.size < items.size) {
                    item(span = { GridItemSpan(columnCount) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(vertical = 16.dp, horizontal = 32.dp)
                                .shimmer()
                                .clip(Shapes.medium)
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                        )
                    }
                }
                item(
                    span = { GridItemSpan(columnCount) },
                ) {
                    Spacer(Modifier.height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
                }
            }
        )


    }

    if (selectedIndex != -1) {
        FullImageScreen(
            selectedImageIndex = selectedIndex,
            image = items
        ) {
            selectedIndex = -1
        }
    }

    LaunchedEffect(scrollableState) {
        snapshotFlow {
            scrollableState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= loadedItemCount - 10 &&
                    loadedItemCount < items.size
                ) {
                    loadedItemCount = (loadedItemCount + 20).coerceAtMost(items.size)
                }
            }
    }
}

@Composable
private fun ImageCardContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        SubcomposeAsyncImage(
            model = imageRequestCrossfade(imageUrl),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = {
                ErrorImagePlaceholder()
            },
            success = {
                SubcomposeAsyncImageContent(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onClick(imageUrl) }
                )
            },
            loading = {
                LoadingImagePlaceholder()
            }
        )
    }
}

@Composable
private fun LoadingImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shimmer()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    )
}

@Composable
private fun ErrorImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                tint = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ошибка загрузки",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
    }
}