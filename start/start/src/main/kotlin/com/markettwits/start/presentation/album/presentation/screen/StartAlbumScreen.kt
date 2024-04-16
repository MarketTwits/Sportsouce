package com.markettwits.start.presentation.album.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.presentation.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.presentation.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.start.presentation.album.presentation.components.StartAlbumScreenContentNew
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore

@Composable
fun StartAlbumScreen(component: StartAlbumComponent) {
    val state by component.state.collectAsState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            TopBarWithClip(title = "Альбом") {
                component.obtainEvent(StartAlbumStore.Intent.GoBack)
            }
        }) {
        StartAlbumScreenContentNew(
            items = state.images
        )
    }
}