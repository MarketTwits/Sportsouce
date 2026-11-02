package com.markettwits.sportsouce.start.presentation.album.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.SaucePhotoViewerBackdrop
import com.markettwits.core_ui.items.screens.photoViewerHostInsets
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.sportsouce.start.presentation.album.presentation.components.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore

@Composable
fun StartAlbumScreen(component: StartAlbumComponent) {
    val state by component.state.collectAsState()
    val systemBarInsets = photoViewerHostInsets()
    val horizontalInsets = WindowInsets.safeDrawing.union(systemBarInsets).only(WindowInsetsSides.Horizontal)
    SaucePhotoViewerBackdrop(Modifier.fillMaxSize()) {
        if (state.album != null || state.isLoading || state.error != null) {
            AdaptivePane(Modifier.fillMaxSize().windowInsetsPadding(horizontalInsets)) {
                PagedAlbumContent(state, state.photos.collectAsLazyPagingItems(),
                    onRetry = { component.obtainEvent(StartAlbumStore.Intent.Retry) },
                    onClear = { component.obtainEvent(StartAlbumStore.Intent.ClearTags) },
                    onEvent = component::obtainEvent)
            }
        } else SauceScaffold(
            contentWindowInsets = horizontalInsets,
            topBar = {
                TopBarBase(modifier = Modifier.windowInsetsPadding(systemBarInsets.only(WindowInsetsSides.Top)),
                    title = "Фотоальбом", isStatusBarHandle = false,
                    goBack = { component.obtainEvent(StartAlbumStore.Intent.GoBack) })
            }
        ) { padding ->
            AdaptivePane(Modifier.fillMaxSize().padding(padding)) {
                if (state.images.isNotEmpty()) StartAlbumScreenContent(items = state.images, selection = state.selectedPhotos, onEvent = component::obtainEvent)
                else AlbumEmptyCard()
            }
        }
    }
    AlbumExportDialog(state, component::obtainEvent)
}
