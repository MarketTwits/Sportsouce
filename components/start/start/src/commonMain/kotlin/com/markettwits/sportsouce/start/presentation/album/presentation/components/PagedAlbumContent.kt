package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import app.cash.paging.*
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.screens.PhotoHeaderSystemBars
import com.markettwits.core_ui.items.screens.SaucePhotoViewer
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.presentation.album.domain.AlbumPhoto
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumDetails
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore
import kotlinx.coroutines.launch

@Composable
internal fun AlbumTagFilters(tags: List<String>, selected: Set<String>, onToggle: (String) -> Unit, onClear: () -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AlbumTagChip("Все фото", selected.isEmpty(), onClear)
        tags.forEach { tag -> AlbumTagChip(tag, tag in selected) { onToggle(tag) } }
    }
}

@Composable
private fun AlbumTagChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, shape = CircleShape, border = null,
        modifier = Modifier.heightIn(min = 48.dp),
        label = { Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1) },
        leadingIcon = { Icon(if (selected) Icons.Outlined.Check else Icons.Outlined.Tag, null, Modifier.size(18.dp)) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = SauceTheme.colors.surfaceSubtle, labelColor = SauceTheme.colors.textSecondary,
            iconColor = SauceTheme.colors.textMuted, selectedContainerColor = SauceTheme.colors.actionSoft,
            selectedLabelColor = SauceTheme.colors.onActionSoft, selectedLeadingIconColor = SauceTheme.colors.onActionSoft))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PagedAlbumContent(
    albumState: StartAlbumStore.State,
    photos: LazyPagingItems<AlbumPhoto>,
    onRetry: () -> Unit,
    onClear: () -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    overscroll: AlbumOverscrollEffect = remember { AlbumOverscrollEffect() },
    onEvent: (StartAlbumStore.Intent) -> Unit = {},
) {
    val refreshing = photos.loadState.refresh is LoadStateLoading
    val loading = albumState.isLoading || refreshing
    var selectedIndex by rememberSaveable(albumState.photos) { mutableIntStateOf(-1) }
    val scope = rememberCoroutineScope()
    val sources = remember { AlbumPhotoSources() }
    val selection = albumState.selectedPhotos
    val selectionMode = selection.isNotEmpty()
    val loadingMetadata = albumState.album == null && albumState.isLoading
    val album = albumState.album ?: if (loadingMetadata) StartAlbumDetails(
        "Фотоальбом", "", "", 0, emptyList(), albumState.images.firstOrNull().orEmpty()) else null
    // Tags change the paging generation, but leave the cover at its current collapse position.
    var previousTags by remember { mutableStateOf(albumState.selectedTags) }
    var filtersHeight by remember { mutableIntStateOf(0) }
    var countHeight by remember { mutableIntStateOf(0) }
    var noticeHeight by remember(albumState.error, photos.itemCount, photos.loadState.refresh, albumState.selectedTags) { mutableIntStateOf(0) }
    LaunchedEffect(albumState.selectedTags) {
        if (previousTags != albumState.selectedTags && gridState.firstVisibleItemIndex > 0) {
            gridState.scrollToItem(if (album != null) 1 else 0)
        }
        previousTags = albumState.selectedTags
    }
    val headerItems = (if (album == null) 0 else 3) + if (albumState.error != null) 1 else 0
    val density = LocalDensity.current
    val toolbarHeight = 56.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val pinnedHeaderHeight = with(density) { toolbarHeight.roundToPx() } + filtersHeight
    SauceTopBarOverlayHost {
        BoxWithConstraints(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
            val coverHeight = if (album != null) {
                minOf(441.dp + (toolbarHeight - 56.dp), maxHeight * .72f)
                    .coerceAtLeast(toolbarHeight + 240.dp * density.fontScale)
            } else toolbarHeight
            val captionHeight = coverHeight - toolbarHeight - if (album != null) 25.dp else 0.dp
            val noticeModifier = Modifier.fillMaxWidth().onSizeChanged { noticeHeight = it.height }
            val columns = with(AlbumGridCells) { with(density) { calculateCrossAxisCellSizes(maxWidth.roundToPx(), 1.dp.roundToPx()) } }
            val cells = if (loading && photos.itemCount == 0) 12 else photos.itemCount
            val rows = (cells + columns.size - 1) / columns.size
            val rowsHeight = with(density) { ((columns.firstOrNull() ?: 0) * rows).toDp() } + (rows - 1).coerceAtLeast(0).dp
            // A short or empty filtered result still leaves enough scroll range to keep the cover collapsed.
            val fillHeight = (maxHeight - toolbarHeight - with(density) { (filtersHeight + countHeight + noticeHeight).toDp() } - rowsHeight).coerceAtLeast(0.dp)
            val extent = with(density) { captionHeight.toPx() }
            val offset = { albumCoverScrollOffset(gridState, extent) }
            val progress = { if (extent > 0f) offset() / extent else 1f }
            val darkTheme = LocalDarkOrLightTheme.current
            val lightStatusIcons by remember(gridState, extent, darkTheme, album != null) { derivedStateOf {
                darkTheme || (album != null && albumCoverPinnedAlpha(if (extent > 0f) albumCoverScrollOffset(gridState, extent) / extent else 1f) < .5f)
            } }
            if (selectedIndex < 0) PhotoHeaderSystemBars(lightIcons = lightStatusIcons)
            SideEffect { overscroll.viewportHeight = with(density) { maxHeight.toPx() } }
            val stretch = { overscroll.offset.coerceAtLeast(0f) }
            SauceTopBarBackdrop(Modifier.fillMaxSize().clipToBounds()) {
                if (album != null) AlbumCoverBackground(album.coverImage, coverHeight + with(density) { stretch().toDp() }, offset, progress)
                // The full-bleed source scrolls beneath overlay glass; chrome never captures itself.
                CompositionLocalProvider(LocalSauceTopBar provides null) {
                    SauceLazyVerticalGrid(columns = AlbumGridCells, state = gridState, modifier = Modifier.fillMaxSize()
                        .offset { IntOffset(0, overscroll.offset.roundToInt()) }
                        .albumSelectionGesture(gridState, sources, photos.itemSnapshotList.items, selection, albumState.exportProgress == null,
                        visibleTop = { pinnedHeaderHeight.toFloat() }) { onEvent(StartAlbumStore.Intent.SelectPhotos(it)) },
                        overscrollEffect = overscroll,
                        contentPadding = PaddingValues(top = toolbarHeight, bottom = if (selectionMode) 100.dp else 0.dp), horizontalArrangement = Arrangement.spacedBy(1.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        if (album != null) {
                            item(key = "cover", span = { GridItemSpan(maxLineSpan) }, contentType = "cover") {
                                AlbumCoverCaption(album, captionHeight, offset, progress, loadingMetadata, stretch)
                            }
                            item(key = "tags", span = { GridItemSpan(maxLineSpan) }, contentType = "tags") {
                                Spacer(Modifier.height(with(density) { filtersHeight.toDp() }))
                            }
                            item(key = "count", span = { GridItemSpan(maxLineSpan) }, contentType = "count") {
                                val count = albumState.filteredCount?.let { "$it фото" }
                                    ?: if (albumState.selectedTags.isEmpty()) "${album.photosCount} фото" else "Поиск фотографий…"
                                Box(Modifier.fillMaxWidth().onSizeChanged { countHeight = it.height }.background(SauceTheme.colors.background).padding(horizontal = 16.dp, vertical = 12.dp)) {
                                    if (loadingMetadata) SauceSkeleton(Modifier.width(72.dp).height(18.dp))
                                    else Text(count, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                                }
                            }
                        }
                        when {
                            loading && photos.itemCount == 0 -> items(12, contentType = { "shimmer" }) {
                                SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(1f), shape = AlbumPhotoShape)
                            }
                            albumState.error != null -> item(span = { GridItemSpan(maxLineSpan) }) {
                                AlbumPhotoNotice("Не удалось открыть альбом", albumState.error, "Повторить", onRetry, noticeModifier)
                            }
                            photos.loadState.refresh is LoadStateError && photos.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                                AlbumPhotoNotice("Не удалось загрузить фотографии", "Проверьте соединение и повторите попытку.", "Повторить", photos::retry, noticeModifier)
                            }
                            !loading && photos.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                                if (albumState.selectedTags.isEmpty()) AlbumEmptyCard(noticeModifier)
                                else AlbumPhotoNotice("Фото не найдены", "Попробуйте выбрать другие теги.", "Все фото", onClear, noticeModifier)
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
                        if (album != null) item(key = "fill", span = { GridItemSpan(maxLineSpan) }, contentType = "fill") {
                            Spacer(Modifier.height(fillHeight))
                        }
                    }
                }
            }
            if (album != null) SauceTopBarOverlay(
                modifier = Modifier.fillMaxWidth().offset {
                    IntOffset(0, (with(density) { (toolbarHeight + captionHeight).toPx() } - offset() + stretch())
                        .coerceAtLeast(with(density) { toolbarHeight.toPx() }).roundToInt())
                }.onSizeChanged { filtersHeight = it.height }, primary = false,
                glassAlpha = { albumCoverPinnedAlpha(progress()) },
            ) {
                AlbumTagFilters(album.tags, albumState.selectedTags,
                    onToggle = { onEvent(StartAlbumStore.Intent.ToggleTag(it)) }, onClear = onClear)
            }
            AlbumCoverToolbar(album?.name ?: "Фотоальбом", progress, album != null,
                goBack = { onEvent(StartAlbumStore.Intent.GoBack) },
                onShare = if ((albumState.albumId ?: 0) > 0) { { onEvent(StartAlbumStore.Intent.ShareAlbum) } } else null)
            if (selectionMode) AlbumSelectionPanel(selection.size, { onEvent(StartAlbumStore.Intent.ClearSelection) },
                { onEvent(StartAlbumStore.Intent.ShareSelection) }, { onEvent(StartAlbumStore.Intent.DownloadSelection) }, Modifier.align(Alignment.BottomCenter))
        }
    }
    if (selectedIndex in 0 until photos.itemCount) {
        SaucePhotoViewer(images = photos.itemSnapshotList.items.map { it.url }, initialIndex = selectedIndex,
            title = albumState.album?.name ?: "Фотоальбом",
            totalCount = albumState.filteredCount ?: photos.itemCount,
            onShare = { index -> photos.peek(index)?.let { onEvent(StartAlbumStore.Intent.SharePhotos(listOf(it))) } },
            onDownload = { index -> photos.peek(index)?.let { onEvent(StartAlbumStore.Intent.DownloadPhotos(listOf(it))) } },
            transitionSource = sources[selectedIndex],
            prepareReturn = { index ->
                gridState.revealAlbumPhoto(index + headerItems, pinnedHeaderHeight)
                withFrameNanos { }; withFrameNanos { }
                sources[index]
            },
            appendError = photos.loadState.append is LoadStateError, onRetry = photos::retry,
            onPageChanged = { index ->
                // Prefetch before the viewer reaches the last loaded photo.
                if (photos.itemCount > 0) photos[(index + 6).coerceAtMost(photos.itemCount - 1)]
            }, onDismiss = { index ->
                scope.launch {
                    val gridIndex = index + headerItems
                    gridState.revealAlbumPhoto(gridIndex, pinnedHeaderHeight)
                    selectedIndex = -1
                }
            })
    }
}

@Composable
private fun AlbumPhotoNotice(title: String, description: String, action: String, onAction: () -> Unit, modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            SauceButton(action, onAction, variant = SauceButtonVariant.Outlined)
        }
    }
}
