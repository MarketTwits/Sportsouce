package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.common.components.StartPhoto

@Composable
internal fun StartAlbums(modifier: Modifier = Modifier, albums: List<StartItem.Album>, onCLickFullAlbum: (StartItem.Album) -> Unit) {
    val first = albums.firstOrNull() ?: return
    var allAlbums by rememberSaveable(albums.map { it.id }) { mutableStateOf(false) }
    var selectedIndex by rememberSaveable(first.id, first.photos) { mutableIntStateOf(-1) }
    val preview = first.photos.take(6).reversed()
    val available = albums.filter { it.photos.isNotEmpty() }
    StartContentBasePanel(modifier, label = first.name, icon = Icons.Outlined.PhotoLibrary) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            if (preview.isEmpty()) Text("В альбоме пока нет фотографий", Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textMuted)
            else LazyRow(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                itemsIndexed(preview, key = { index, photo -> "${photo.id}:$index" }) { index, photo ->
                    OnBackgroundCard(Modifier.width(180.dp).semantics { contentDescription = "Фото ${index + 1} из ${preview.size}" }, onClick = { selectedIndex = index }) {
                        Box(Modifier.fillMaxWidth().height(220.dp)) {
                            StartPhoto(photo.imageUrl, Modifier.fillMaxSize())
                            photo.tags.values.firstOrNull()?.takeIf { it.isNotBlank() }?.let { tag ->
                                SauceStatusBadge(if (tag == "preview") "Превью" else tag,
                                    Modifier.align(Alignment.TopStart).padding(SauceSpacing.small), showIndicator = false)
                            }
                        }
                    }
                }
                if (first.photos.size > 6) item {
                    OnBackgroundCard(Modifier.width(180.dp), onClick = { onCLickFullAlbum(first) }) {
                        Box(Modifier.fillMaxWidth().height(220.dp).padding(SauceSpacing.large), contentAlignment = Alignment.Center) {
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                                Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.action)
                                Text("Ещё фото", Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall,
                                    color = SauceTheme.colors.action, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                }
            }
            if (available.size > 1 || (first.photos.isEmpty() && available.isNotEmpty())) {
                SauceActionRow("Все альбомы", { allAlbums = true }, icon = Icons.Outlined.PhotoLibrary, count = available.size.toString())
            }
        }
    }
    if (selectedIndex in preview.indices) FullImageScreen(image = preview.map { it.imageUrl }, selectedImageIndex = selectedIndex) { selectedIndex = -1 }
    if (allAlbums) DefaultModalBottomSheet(title = "Все альбомы", onDismissRequest = { allAlbums = false }) {
        StartAlbumChoices(albums, onCLickFullAlbum = { onCLickFullAlbum(it); allAlbums = false })
    }
}

@Composable
internal fun StartAlbumChoices(albums: List<StartItem.Album>, modifier: Modifier = Modifier, onCLickFullAlbum: (StartItem.Album) -> Unit) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val columns = if (maxWidth < 340.dp * LocalDensity.current.fontScale) 1 else 2
        val rows = albums.filter { it.photos.isNotEmpty() }.chunked(columns)
        val photoHeight = ((maxWidth - SauceSpacing.large * 2 - SauceSpacing.medium * (columns - 1)) / columns) * 3 / 4
        LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            items(rows, key = { it.first().id }) { row ->
                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                    row.forEach { album ->
                        OnBackgroundCard(Modifier.weight(1f).fillMaxHeight(), onClick = { onCLickFullAlbum(album) }) {
                            StartPhoto(album.photos.first().imageUrl, Modifier.fillMaxWidth().height(photoHeight))
                            Column(Modifier.fillMaxWidth().weight(1f).padding(SauceSpacing.medium)) {
                                Text(album.name, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                                Spacer(Modifier.weight(1f).heightIn(min = SauceSpacing.extraSmall))
                                Text("${album.photos.size} фото", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                            }
                        }
                    }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}
