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
import com.markettwits.core_ui.items.screens.AdaptivePane
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
        AdaptivePane(Modifier.fillMaxSize().padding(padding)) {
            Column(Modifier.fillMaxSize()) {
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
        SauceLazyVerticalGrid(
            columns = GridCells.Adaptive(280.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when {
                refreshing && items.itemCount == 0 -> items(3, contentType = { "shimmer" }) { StartAlbumShimmer() }
                items.loadState.refresh is LoadStateError && items.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                    ArchiveNotice("Не удалось загрузить фотоархив", "Проверьте соединение и повторите попытку.", "Повторить", items::retry)
                }
                !refreshing && items.itemCount == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
                    ArchiveNotice("Альбомов не найдено", "Попробуйте другое название старта или выберите другой год.")
                }
            }
            for (index in 0 until items.itemCount) {
                val start = items.peek(index) ?: continue
                item(key = "start-${start.id}", contentType = "header", span = { GridItemSpan(maxLineSpan) }) {
                    // Access through Paging when visible, so headers also advance prefetch.
                    items[index]?.let { current ->
                        Column(Modifier.fillMaxWidth().padding(top = if (index == 0) 0.dp else 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(current.name, style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
                            Text(listOf(current.date, "${current.albums.sumOf { it.photosCount }} фото").filter { it.isNotBlank() }.joinToString(" · "),
                                style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                        }
                    }
                }
                items(start.albums.size, key = { "album-${start.id}-${start.albums[it].id}" }, contentType = { "album" }) { albumIndex ->
                    items[index]?.albums?.getOrNull(albumIndex)?.let { album ->
                        StartAlbumCard(album) { onAlbum(album) }
                    }
                }
            }
            if (items.loadState.refresh is LoadStateError && items.itemCount > 0) item(span = { GridItemSpan(maxLineSpan) }) {
                ArchiveNotice("Не удалось обновить фотоархив", "Загруженные альбомы сохранены.", "Повторить", items::retry)
            }
            if (items.loadState.append is LoadStateLoading) items(2, contentType = { "shimmer" }) { StartAlbumShimmer() }
            if (items.loadState.append is LoadStateError) item(span = { GridItemSpan(maxLineSpan) }) {
                ArchiveNotice("Не удалось загрузить следующие альбомы", "Попробуйте ещё раз.", "Повторить", items::retry)
            }
        }
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
