package com.markettwits.sportsouce.start.presentation.album.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.SaucePhotoViewerBackdrop
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.sportsouce.start.presentation.album.presentation.components.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore

@Composable
fun StartAlbumScreen(component: StartAlbumComponent) {
    val state by component.state.collectAsState()
    var headerCollapsed by rememberSaveable { mutableStateOf(false) }
    SaucePhotoViewerBackdrop(Modifier.fillMaxSize()) {
        SauceScaffold(
            contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
            topBar = {
                Column {
                    val album = state.album
                    val title = if (headerCollapsed) album?.startName?.takeIf { it.isNotBlank() } else null
                    TopBarBase(title = title ?: album?.name ?: "Фотоальбом", goBack = { component.obtainEvent(StartAlbumStore.Intent.GoBack) })
                    if (album?.tags.orEmpty().isNotEmpty()) AlbumTagFilters(
                        album!!.tags, state.selectedTags,
                        onToggle = { component.obtainEvent(StartAlbumStore.Intent.ToggleTag(it)) },
                        onClear = { component.obtainEvent(StartAlbumStore.Intent.ClearTags) })
                    else Spacer(Modifier.height(8.dp))
                }
            }) { padding ->
            AdaptivePane(Modifier.fillMaxSize().padding(padding)) {
                if (state.album != null || state.isLoading || state.error != null) {
                    key(state.photos) {
                        PagedAlbumContent(state, state.photos.collectAsLazyPagingItems(),
                            onRetry = { component.obtainEvent(StartAlbumStore.Intent.Retry) },
                            onClear = { component.obtainEvent(StartAlbumStore.Intent.ClearTags) },
                            onHeaderCollapsed = { headerCollapsed = it }, onEvent = component::obtainEvent)
                    }
                } else if (state.images.isNotEmpty()) StartAlbumScreenContent(items = state.images, selection = state.selectedPhotos, onEvent = component::obtainEvent)
                else AlbumEmptyCard()
            }
        }
    }
    AlbumExportDialog(state, component::obtainEvent)
}
