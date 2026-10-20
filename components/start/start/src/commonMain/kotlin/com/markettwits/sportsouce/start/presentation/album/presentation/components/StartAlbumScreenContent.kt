package com.markettwits.sportsouce.start.presentation.album.presentation.components

import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid as LazyVerticalGrid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.ScrollToTopBabButton
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.presentation.common.components.StartPhoto
import kotlinx.coroutines.launch

@Composable
fun StartAlbumScreenContent(modifier: Modifier = Modifier, items: List<String>) {
    key(items) {
        BoxWithConstraints(modifier.fillMaxSize()) {
            var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
            val state = rememberLazyGridState()
            val scope = rememberCoroutineScope()
            val columns = (maxWidth / 140.dp).toInt().coerceIn(2, 5)
            val showScrollToTop by remember(columns) { derivedStateOf { state.firstVisibleItemIndex > columns * 2 } }
            Scaffold(Modifier.fillMaxSize(), containerColor = SauceTheme.colors.background,
                floatingActionButton = {
                    if (showScrollToTop) ScrollToTopBabButton { scope.launch { state.animateScrollToItem(0) } }
                }) { padding ->
                StartAlbumPhotoGrid(items, state, onSelectImage = { selectedIndex = it }, modifier = Modifier.padding(padding))
            }
            if (selectedIndex in items.indices) FullImageScreen(image = items, selectedImageIndex = selectedIndex) { selectedIndex = -1 }
        }
    }
}

/** Rendering remains incremental; a new album resets the visible batch and selected index. */
@Composable
internal fun StartAlbumPhotoGrid(items: List<String>, state: LazyGridState, onSelectImage: (Int) -> Unit, modifier: Modifier = Modifier) {
    var loadedItemCount by rememberSaveable(items) { mutableIntStateOf(20) }
    val loadedItems = items.take(loadedItemCount.coerceAtMost(items.size))
    BoxWithConstraints(modifier.fillMaxSize()) {
        val columns = (maxWidth / 140.dp).toInt().coerceIn(2, 5)
        LazyVerticalGrid(columns = GridCells.Fixed(columns), state = state, modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = SauceSpacing.small, end = SauceSpacing.small,
                top = 64.dp, bottom = SauceSpacing.large),
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            itemsIndexed(loadedItems, key = { index, item -> "$index:$item" }) { index, url ->
                StartPhoto(url, Modifier.fillMaxWidth().aspectRatio(0.75f).clip(SauceShapes.field)
                    .clickable(role = Role.Button) { onSelectImage(index) }
                    .semantics { contentDescription = "Фото ${index + 1} из ${items.size}" })
            }
            if (loadedItems.size < items.size) item(span = { GridItemSpan(columns) }) {
                Box(Modifier.fillMaxWidth().height(48.dp).clip(SauceShapes.field).shimmer().background(SauceTheme.colors.surfaceSubtle))
            }
        }
    }
    LaunchedEffect(state, items) {
        snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.collect { last ->
            if (last != null && last >= loadedItemCount - 10 && loadedItemCount < items.size)
                loadedItemCount = (loadedItemCount + 20).coerceAtMost(items.size)
        }
    }
}
