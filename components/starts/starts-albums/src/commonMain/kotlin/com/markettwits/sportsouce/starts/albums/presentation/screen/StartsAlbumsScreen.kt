package com.markettwits.sportsouce.starts.albums.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import app.cash.paging.*
import app.cash.paging.compose.*
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.albums.domain.StartAlbumsItem
import com.markettwits.sportsouce.starts.albums.presentation.component.StartsAlbumsComponent
import com.markettwits.sportsouce.starts.albums.presentation.components.*
import com.markettwits.sportsouce.starts.albums.presentation.store.StartsAlbumsStore

@Composable
fun StartsAlbumsScreen(component: StartsAlbumsComponent) {
    val state by component.state.collectAsState()
    val focus = LocalFocusManager.current
    val items = state.items.collectAsLazyPagingItems()
    SauceScaffold(topBar = {
        AlbumsSearchBar(state.filterText, state.year != null,
            onQueryChanged = { component.obtainEvent(StartsAlbumsStore.Intent.Search(it)) },
            onFilters = { component.obtainEvent(StartsAlbumsStore.Intent.OpenFilters) },
            onBack = { component.obtainEvent(StartsAlbumsStore.Intent.Back) })
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding), contentAlignment = Alignment.TopCenter) {
            Column(Modifier.widthIn(max = 1350.dp).fillMaxSize()) {
                if (state.year != null) SauceTopBarSection {
                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        com.markettwits.core_ui.items.components.checkbox.FilterChipBase(
                            selected = true, label = "Год: ${state.year}",
                            onClick = { component.obtainEvent(StartsAlbumsStore.Intent.OpenFilters) })
                        Spacer(Modifier.weight(1f))
                        SauceButton("Сбросить", { component.obtainEvent(StartsAlbumsStore.Intent.SelectYear(null)) },
                            variant = SauceButtonVariant.Text, fillWidth = false)
                    }
                }
                key(state.items) { StartsAlbumsContent(items) {
                    focus.clearFocus()
                    component.obtainEvent(StartsAlbumsStore.Intent.OpenAlbum(it))
                } }
            }
        }
    }
}

@Composable
internal fun StartsAlbumsContent(items: LazyPagingItems<StartAlbumsItem>, onAlbum: (StartAlbumsItem.Album) -> Unit) {
    val refreshing = items.loadState.refresh is LoadStateLoading
    PullToRefreshScreen(Modifier.fillMaxSize(), isRefreshing = refreshing && items.itemCount > 0, onRefresh = items::refresh) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val gutter = 16.dp
            val gap = 12.dp
            val columns = ((maxWidth - gutter * 2 + gap) / (280.dp + gap)).toInt().coerceAtLeast(1)
            val showStartInCard = columns > 1
            SauceLazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(gutter),
                verticalArrangement = Arrangement.spacedBy(gap),
                horizontalArrangement = Arrangement.spacedBy(gap),
            ) {
                when {
                    refreshing && items.itemCount == 0 -> items(maxOf(3, columns * 2), contentType = { "shimmer" }) {
                        StartAlbumShimmer(showStartInCard)
                    }
                    items.loadState.refresh is LoadStateError && items.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                        ArchiveNotice("Не удалось загрузить фотоархив", "Проверьте соединение и повторите попытку.", "Повторить", items::retry)
                    }
                    !refreshing && items.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                        ArchiveNotice("Альбомов не найдено", "Попробуйте другое название старта или выберите другой год.")
                    }
                }
                for (index in 0 until items.itemCount) {
                    val start = items.peek(index) ?: continue
                    if (!showStartInCard) {
                        item(key = "start-${start.id}", contentType = "header", span = { GridItemSpan(maxLineSpan) }) {
                            items[index]?.let { StartAlbumGroupHeading(it, first = index == 0) }
                        }
                    }
                    items(start.albums.size, key = { "album-${start.id}-${start.albums[it].id}" }, contentType = { "album" }) { albumIndex ->
                        val current = items[index]
                        current?.albums?.getOrNull(albumIndex)?.let { album ->
                            StartAlbumCard(album, start = current.takeIf { showStartInCard }) { onAlbum(album) }
                        }
                    }
                }
                if (items.loadState.refresh is LoadStateError && items.itemCount > 0) item(span = { GridItemSpan(maxLineSpan) }) {
                    ArchiveNotice("Не удалось обновить фотоархив", "Загруженные альбомы сохранены.", "Повторить", items::retry)
                }
                if (items.loadState.append is LoadStateLoading) items(maxOf(2, columns), contentType = { "shimmer" }) {
                    StartAlbumShimmer(showStartInCard)
                }
                if (items.loadState.append is LoadStateError) item(span = { GridItemSpan(maxLineSpan) }) {
                    ArchiveNotice("Не удалось загрузить следующие альбомы", "Попробуйте ещё раз.", "Повторить", items::retry)
                }
            }
        }
    }
}

@Composable
private fun StartAlbumGroupHeading(start: StartAlbumsItem, first: Boolean) {
    Column(
        Modifier.fillMaxWidth().padding(top = if (first) 0.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(start.name, style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
        Text(listOf(start.date, "${start.albums.sumOf { it.photosCount }} фото").filter { it.isNotBlank() }.joinToString(" · "),
            style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
    }
}

@Composable
private fun ArchiveNotice(title: String, description: String, action: String? = null, onAction: () -> Unit = {}) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            if (action != null) SauceButton(action, onAction, variant = SauceButtonVariant.Outlined)
        }
    }
}
