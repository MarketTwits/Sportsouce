package com.markettwits.sportsouce.start.presentation.album.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponent
import com.markettwits.sportsouce.start.presentation.album.presentation.components.AlbumEmptyCard
import com.markettwits.sportsouce.start.presentation.album.presentation.components.StartAlbumScreenContent
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore

@Composable
fun StartAlbumScreen(component: StartAlbumComponent) {
    val state by component.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.images.isEmpty()) {
            AlbumEmptyCard()
        } else {
            StartAlbumScreenContent(items = state.images)
        }

        BackFloatingActionButton(
            modifier = Modifier.align(Alignment.TopStart),
            back = {
                component.obtainEvent(StartAlbumStore.Intent.GoBack)
            }
        )

    }

//    CollapsingToolbarScaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background),
//        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
//        state = scrollState,
//        toolbar = {
//            TopBarWithClip(title = "Альбом") {
//                component.obtainEvent(StartAlbumStore.Intent.GoBack)
//            }
//        }
//    ) {
//
//    }
}