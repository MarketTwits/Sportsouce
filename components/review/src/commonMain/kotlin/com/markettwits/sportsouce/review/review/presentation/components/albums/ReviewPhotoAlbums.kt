package com.markettwits.sportsouce.review.review.presentation.components.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum

@Composable
internal fun ReviewPhotoAlbums(
    items: List<ReviewPhotoAlbum>,
    loading: Boolean,
    failed: Boolean,
    onClickAlbum: (Int) -> Unit,
    onClickAll: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Фото со стартов", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary)
            TextButton(onClick = onClickAll) { Text("Все альбомы", color = SauceTheme.colors.action) }
        }
        if (failed && items.isEmpty()) {
            SauceActionRow("Не удалось загрузить альбомы", onRetry, Modifier.padding(horizontal = 16.dp), subtitle = "Нажмите, чтобы повторить")
        } else BoxWithConstraints(Modifier.fillMaxWidth()) {
            val width = (maxWidth * 0.68f).coerceAtMost(340.dp)
            val skeletons = loading && items.isEmpty()
            LazyRow(Modifier.fillMaxWidth().then(if (skeletons) Modifier.semantics {
                contentDescription = "Загрузка фотоальбомов"
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            } else Modifier), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (skeletons) items(3, key = { "album-placeholder-$it" }) {
                    PhotoAlbumCard(null, Modifier.width(width), null)
                }
                items(items, key = { it.id }) { album ->
                    PhotoAlbumCard(album, Modifier.width(width)) { onClickAlbum(album.id) }
                }
            }
        }
    }
}

@Composable
private fun PhotoAlbumCard(album: ReviewPhotoAlbum?, modifier: Modifier, onClick: (() -> Unit)?) {
    val titleHeight = with(LocalDensity.current) { 40.sp.toDp() }
    val dateHeight = with(LocalDensity.current) { 16.sp.toDp() }
    OnBackgroundCard(modifier, onClick = onClick) {
        Box(Modifier.fillMaxWidth().aspectRatio(16f / 11f).background(SauceTheme.colors.surfaceSubtle)) {
            if (album == null) SauceSkeleton(Modifier.fillMaxSize(), SauceShapes.field)
            else {
                AlbumImage(album.previewUrl, AlbumImageSize.Cover, Modifier.fillMaxSize())
                Surface(Modifier.align(Alignment.TopEnd).padding(10.dp), color = Color.Black.copy(alpha = 0.55f),
                    contentColor = Color.White, shape = SauceShapes.badge) {
                    Row(Modifier.padding(horizontal = 8.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(14.dp))
                        Text("${album.photosCount} фото", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (album == null) {
                Column(Modifier.fillMaxWidth().height(titleHeight), verticalArrangement = Arrangement.SpaceEvenly) {
                    SauceSkeleton(Modifier.fillMaxWidth(.9f).height(titleHeight * .3f))
                    SauceSkeleton(Modifier.fillMaxWidth(.65f).height(titleHeight * .3f))
                }
                SauceSkeleton(Modifier.fillMaxWidth(.55f).height(dateHeight))
            } else {
                Text(album.startName, Modifier.fillMaxWidth().heightIn(min = titleHeight), style = MaterialTheme.typography.titleSmall.copy(lineHeight = 20.sp),
                    color = SauceTheme.colors.textPrimary, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(listOf(album.date, album.name.takeUnless { it.equals("Фото", ignoreCase = true) }.orEmpty())
                    .filter { it.isNotBlank() }.joinToString(" · "), Modifier.fillMaxWidth().heightIn(min = dateHeight),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp), color = SauceTheme.colors.textSecondary,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
