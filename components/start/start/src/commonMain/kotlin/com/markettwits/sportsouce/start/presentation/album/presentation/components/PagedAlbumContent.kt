package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import app.cash.paging.*
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.screens.SaucePhotoViewer
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.presentation.album.domain.AlbumPhoto
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore
import com.markettwits.sportsouce.start.presentation.common.components.StartPhoto
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun AlbumTagFilters(tags: List<String>, selected: Set<String>, onToggle: (String) -> Unit, onClear: () -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChipBase(selected = selected.isEmpty(), label = "Все фото", onClick = onClear)
        tags.forEach { tag -> FilterChipBase(selected = tag in selected, label = tag, onClick = { onToggle(tag) }) }
    }
}

@Composable
internal fun PagedAlbumContent(
    albumState: StartAlbumStore.State,
    photos: LazyPagingItems<AlbumPhoto>,
    onRetry: () -> Unit,
    onClear: () -> Unit,
    onHeaderCollapsed: (Boolean) -> Unit = {},
    onEvent: (StartAlbumStore.Intent) -> Unit = {},
) {
    val refreshing = photos.loadState.refresh is LoadStateLoading
    val loading = albumState.isLoading || refreshing
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val sources = remember { AlbumPhotoSources() }
    val selection = albumState.selectedPhotos
    val selectionMode = selection.isNotEmpty()
    val headerCollapsed by rememberUpdatedState(onHeaderCollapsed)
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemIndex > 0 }.distinctUntilChanged().collect { headerCollapsed(it) }
    }
    Box(Modifier.fillMaxSize()) {
        PullToRefreshScreen(Modifier.fillMaxSize(), isRefreshing = refreshing && photos.itemCount > 0,
            onRefresh = { if (albumState.error != null) onRetry() else photos.refresh() }) {
            SauceLazyVerticalGrid(columns = AlbumGridCells, state = gridState, modifier = Modifier.fillMaxSize()
                .albumSelectionGesture(gridState, sources, photos.itemSnapshotList.items, selection, albumState.exportProgress == null) { onEvent(StartAlbumStore.Intent.SelectPhotos(it)) },
                contentPadding = PaddingValues(top = 12.dp, bottom = if (selectionMode) 100.dp else 0.dp), horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)) {
                albumState.album?.let { album ->
                    item(key = "header", span = { GridItemSpan(maxLineSpan) }, contentType = "header") {
                        Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (album.startName.isNotBlank()) Text(album.startName, style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
                            val count = albumState.filteredCount?.let { "$it фото" }
                                ?: if (albumState.selectedTags.isEmpty()) "${album.photosCount} фото" else "Поиск фотографий…"
                            Text(listOf(album.date, count).filter { it.isNotBlank() }.joinToString(" · "),
                                style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                        }
                    }
                }
                when {
                    loading && photos.itemCount == 0 -> items(12, contentType = { "shimmer" }) {
                        SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(1f), shape = AlbumPhotoShape)
                    }
                    albumState.error != null -> item(span = { GridItemSpan(maxLineSpan) }) {
                        AlbumPhotoNotice("Не удалось открыть альбом", albumState.error, "Повторить", onRetry)
                    }
                    photos.loadState.refresh is LoadStateError && photos.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                        AlbumPhotoNotice("Не удалось загрузить фотографии", "Проверьте соединение и повторите попытку.", "Повторить", photos::retry)
                    }
                    !loading && photos.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                        if (albumState.selectedTags.isEmpty()) AlbumEmptyCard()
                        else AlbumPhotoNotice("Фото не найдены", "Попробуйте выбрать другие теги.", "Все фото", onClear)
                    }
                }
                items(photos.itemCount, key = { index -> "photo-${photos.peek(index)?.id ?: index}" }, contentType = { "photo" }) { index ->
                    photos[index]?.let { photo ->
                        AlbumPhotoTile(index, photo.url, "Фото ${index + 1}", sources, selectionMode, photo.id in selection,
                            onLongClick = { onEvent(StartAlbumStore.Intent.TogglePhoto(photo)) }) {
                            if (selectionMode) onEvent(StartAlbumStore.Intent.TogglePhoto(photo)) else selectedIndex = index
                        }
                    }
                }
                if (photos.loadState.append is LoadStateLoading) items(4, contentType = { "shimmer" }) {
                    SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(1f), shape = AlbumPhotoShape)
                }
                if (photos.loadState.append is LoadStateError || (photos.loadState.refresh is LoadStateError && photos.itemCount > 0)) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AlbumPhotoNotice("Не удалось загрузить фотографии", "Уже загруженные фото доступны.", "Повторить", photos::retry)
                    }
                }
            }
        }
        if (selectionMode) AlbumSelectionPanel(selection.size, { onEvent(StartAlbumStore.Intent.ClearSelection) },
            { onEvent(StartAlbumStore.Intent.ShareSelection) }, { onEvent(StartAlbumStore.Intent.DownloadSelection) }, Modifier.align(Alignment.BottomCenter))
    }
    if (selectedIndex in 0 until photos.itemCount) {
        SaucePhotoViewer(images = photos.itemSnapshotList.items.map { it.url }, initialIndex = selectedIndex,
            title = albumState.album?.name ?: "Фотоальбом",
            totalCount = albumState.filteredCount ?: photos.itemCount,
            onShare = { index -> photos.peek(index)?.let { onEvent(StartAlbumStore.Intent.SharePhotos(listOf(it))) } },
            onDownload = { index -> photos.peek(index)?.let { onEvent(StartAlbumStore.Intent.DownloadPhotos(listOf(it))) } },
            transitionSource = sources[selectedIndex],
            prepareReturn = { index ->
                gridState.revealAlbumPhoto(index + if (albumState.album != null) 1 else 0)
                withFrameNanos { }; withFrameNanos { }
                sources[index]
            },
            appendError = photos.loadState.append is LoadStateError, onRetry = photos::retry,
            onPageChanged = { index ->
                // Prefetch before the viewer reaches the last loaded photo.
                if (photos.itemCount > 0) photos[(index + 6).coerceAtMost(photos.itemCount - 1)]
            }, onDismiss = { index ->
                scope.launch {
                    val gridIndex = index + if (albumState.album != null) 1 else 0
                    gridState.revealAlbumPhoto(gridIndex)
                    selectedIndex = -1
                }
            })
    }
}

@Composable
private fun AlbumPhotoNotice(title: String, description: String, action: String, onAction: () -> Unit) {
    OnBackgroundCard {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            SauceButton(action, onAction, variant = SauceButtonVariant.Outlined)
        }
    }
}
