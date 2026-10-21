package com.markettwits.sportsouce.start.presentation.album.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.grid.GridCells
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
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
            .background(SauceTheme.colors.background)
    ) {
        if (state.isLoading) {
            SauceLazyVerticalGrid(columns = GridCells.Adaptive(140.dp), modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 88.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(8) { SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(.75f)) }
            }
        } else if (state.error != null) {
            Box(Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp), contentAlignment = Alignment.Center) {
                OnBackgroundCard {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Не удалось открыть альбом", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                        Text(state.error.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                        SauceButton("Повторить", { component.obtainEvent(StartAlbumStore.Intent.Retry) })
                    }
                }
            }
        } else if (state.images.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { AlbumEmptyCard() }
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
}