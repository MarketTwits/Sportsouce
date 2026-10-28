package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartAlbums(modifier: Modifier = Modifier, albums: List<StartItem.Album>, onCLickFullAlbum: (StartItem.Album) -> Unit) {
    val available = albums.filter { it.photos.isNotEmpty() }
    if (available.isEmpty()) return
    var allAlbums by rememberSaveable(available.map { it.id }) { mutableStateOf(false) }
    StartContentBasePanel(modifier, label = "Фотоальбомы", icon = Icons.Outlined.PhotoLibrary) {
        if (available.size == 1) StartAlbumPreview(available.single(), onClick = { onCLickFullAlbum(available.single()) })
        else {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val largeText = LocalDensity.current.fontScale > 1.3f
                val titleLines = if (largeText) 3 else 2
                val cardWidth = if (maxWidth < 500.dp) maxWidth * .88f else ((maxWidth - 12.dp) / 2).coerceAtMost(360.dp)
                val textHeight = with(LocalDensity.current) {
                    (MaterialTheme.typography.titleSmall.lineHeight * titleLines).toDp()
                }
                LazyHorizontalGrid(rows = GridCells.Fixed(1), modifier = Modifier.fillMaxWidth()
                    .height(cardWidth / (16f / 10f) + textHeight + 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(available, key = { it.id }) { album ->
                        StartAlbumPreview(album, Modifier.width(cardWidth), titleLines = titleLines, reserveTitleSpace = true,
                            onClick = { onCLickFullAlbum(album) })
                    }
                }
            }
            SauceActionRow("Все альбомы", { allAlbums = true }, icon = Icons.Outlined.PhotoLibrary, count = available.size.toString())
        }
    }
    if (allAlbums) DefaultModalBottomSheet(title = "Фотоальбомы", onDismissRequest = { allAlbums = false }) {
        StartAlbumChoices(available, onCLickFullAlbum = { allAlbums = false; onCLickFullAlbum(it) })
    }
}

/** One opening action per album; the collage shows its contents without a second photo viewer. */
@Composable
internal fun StartAlbumPreview(album: StartItem.Album, modifier: Modifier = Modifier, titleLines: Int = 2,
    reserveTitleSpace: Boolean = false, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    SauceClickTarget(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = SauceShapes.card, onClickLabel = "Открыть альбом") {
        Column {
            Box(Modifier.fillMaxWidth().aspectRatio(16f / 10f).clip(SauceShapes.card).background(colors.surfaceSubtle)) {
                val cover = album.photos.filter { it.imageUrl.isNotBlank() }.distinctBy { it.imageUrl }.take(3)
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    AlbumImage(cover.firstOrNull()?.imageUrl.orEmpty(), AlbumImageSize.Cover,
                        Modifier.weight(if (cover.size > 1) 2f else 1f).fillMaxHeight())
                    if (cover.size > 1) Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        cover.drop(1).forEach { photo ->
                            AlbumImage(photo.imageUrl, AlbumImageSize.Grid, Modifier.fillMaxWidth().weight(1f))
                        }
                    }
                }
                Surface(Modifier.align(Alignment.BottomStart).padding(10.dp), shape = SauceShapes.badge, color = colors.surface.copy(alpha = .94f)) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(16.dp), tint = colors.textSecondary)
                        Text("${album.photos.size} фото",
                            Modifier.width(IntrinsicSize.Max),
                            style = MaterialTheme.typography.bodySmall, color = colors.textPrimary)
                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(start = 2.dp, end = 6.dp, top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(album.name.ifBlank { "Фотоальбом" }, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall,
                    minLines = if (reserveTitleSpace) titleLines else 1, maxLines = titleLines, overflow = TextOverflow.Ellipsis, color = colors.textPrimary)
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, Modifier.size(20.dp), tint = colors.action)
            }
        }
    }
}

@Composable
internal fun StartAlbumChoices(albums: List<StartItem.Album>, modifier: Modifier = Modifier, onCLickFullAlbum: (StartItem.Album) -> Unit) {
    val fontScale = LocalDensity.current.fontScale
    SauceLazyVerticalGrid(columns = GridCells.Adaptive(260.dp * fontScale.coerceAtMost(1.5f)), modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(albums.filter { it.photos.isNotEmpty() }, key = { it.id }) { album ->
            StartAlbumPreview(album, titleLines = if (fontScale > 1.3f) 3 else 2, reserveTitleSpace = true,
                onClick = { onCLickFullAlbum(album) })
        }
    }
}
