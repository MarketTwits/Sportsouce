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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.ScrollToTopBabButton
import com.markettwits.core_ui.items.screens.SaucePhotoViewer
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.presentation.common.components.StartPhoto
import kotlinx.coroutines.launch
import com.markettwits.sportsouce.start.presentation.album.domain.AlbumPhoto
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore

@Composable
fun StartAlbumScreenContent(modifier: Modifier = Modifier, items: List<String>, selection: Map<Int, AlbumPhoto> = emptyMap(), onEvent: (StartAlbumStore.Intent) -> Unit = {}) {
    key(items) {
        val sources = remember { AlbumPhotoSources() }
        BoxWithConstraints(modifier.fillMaxSize()) {
            var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
            val state = rememberLazyGridState()
            val scope = rememberCoroutineScope()
            val columns = if (maxWidth < 600.dp) 3 else (maxWidth / 160.dp).toInt().coerceAtLeast(3)
            val showScrollToTop by remember(columns) { derivedStateOf { state.firstVisibleItemIndex > columns * 2 } }
            Scaffold(Modifier.fillMaxSize(), containerColor = SauceTheme.colors.background,
                contentWindowInsets = WindowInsets(0),
                floatingActionButton = {
                    if (showScrollToTop) ScrollToTopBabButton { scope.launch { state.animateScrollToItem(0) } }
                }) { padding ->
                StartAlbumPhotoGrid(items, state, onSelectImage = {
                    if (selection.isNotEmpty()) onEvent(StartAlbumStore.Intent.TogglePhoto(AlbumPhoto(it, items[it]))) else selectedIndex = it
                }, modifier = Modifier.padding(padding), sources = sources, selection = selection, onEvent = onEvent)
            }
            if (selection.isNotEmpty()) AlbumSelectionPanel(selection.size, { onEvent(StartAlbumStore.Intent.ClearSelection) },
                { onEvent(StartAlbumStore.Intent.ShareSelection) }, { onEvent(StartAlbumStore.Intent.DownloadSelection) }, Modifier.align(Alignment.BottomCenter))
            if (selectedIndex in items.indices) SaucePhotoViewer(images = items, initialIndex = selectedIndex, title = "Фотоальбом",
                onShare = { onEvent(StartAlbumStore.Intent.SharePhotos(listOf(AlbumPhoto(it, items[it])))) },
                onDownload = { onEvent(StartAlbumStore.Intent.DownloadPhotos(listOf(AlbumPhoto(it, items[it])))) },
                transitionSource = sources[selectedIndex], prepareReturn = { index ->
                    state.revealAlbumPhoto(index)
                    withFrameNanos { }; withFrameNanos { }
                    sources[index]
                }) { index ->
                scope.launch {
                    state.revealAlbumPhoto(index)
                    selectedIndex = -1
                }
            }
        }
    }
}

/** Lazy grid cells keep decoding incremental even for albums supplied as a complete list. */
@Composable
internal fun StartAlbumPhotoGrid(items: List<String>, state: LazyGridState, onSelectImage: (Int) -> Unit, modifier: Modifier = Modifier,
    sources: AlbumPhotoSources = remember { AlbumPhotoSources() }, selection: Map<Int, AlbumPhoto> = emptyMap(), onEvent: (StartAlbumStore.Intent) -> Unit = {}) {
    val photos = remember(items) { items.mapIndexed { index, url -> AlbumPhoto(index, url) } }
    LazyVerticalGrid(columns = AlbumGridCells, state = state, modifier = modifier.fillMaxSize()
        .albumSelectionGesture(state, sources, photos, selection) { onEvent(StartAlbumStore.Intent.SelectPhotos(it)) },
        contentPadding = PaddingValues(top = 12.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
            if (selection.isNotEmpty()) 100.dp else 0.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
        itemsIndexed(items, key = { index, _ -> "photo-$index" }) { index, url ->
            AlbumPhotoTile(index, url, "Фото ${index + 1} из ${items.size}", sources, selection.isNotEmpty(), index in selection,
                onLongClick = { onEvent(StartAlbumStore.Intent.TogglePhoto(photos[index])) }) { onSelectImage(index) }
        }
    }
}

/** Preserve the viewport when the photo is already fully visible, otherwise reveal its row. */
internal suspend fun LazyGridState.revealAlbumPhoto(gridIndex: Int, pinnedHeaderHeight: Int = 0) {
    val viewport = layoutInfo
    val safeTop = viewport.viewportStartOffset + pinnedHeaderHeight
    val visible = viewport.visibleItemsInfo.any { it.index == gridIndex &&
        it.offset.y >= safeTop && it.offset.y + it.size.height <= viewport.viewportEndOffset }
    if (!visible) scrollToItem(gridIndex, -safeTop)
}
