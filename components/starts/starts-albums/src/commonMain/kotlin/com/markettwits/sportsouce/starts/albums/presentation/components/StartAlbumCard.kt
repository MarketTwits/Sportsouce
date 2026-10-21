package com.markettwits.sportsouce.starts.albums.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.sportsouce.starts.albums.domain.StartAlbumsItem

@Composable
internal fun StartAlbumCard(album: StartAlbumsItem.Album, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OnBackgroundCard(modifier = modifier, onClick = onClick) {
        AlbumImage(album.previewUrl, AlbumImageSize.Cover, Modifier.fillMaxWidth().aspectRatio(16f / 11f))
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(album.name, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary,
                maxLines = 2, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(18.dp), tint = SauceTheme.colors.textMuted)
                Text("${album.photosCount} фото", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
            }
        }
    }
}

@Composable
internal fun StartAlbumShimmer() {
    OnBackgroundCard {
        SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(16f / 11f))
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SauceSkeleton(Modifier.fillMaxWidth(.7f).height(20.dp))
            SauceSkeleton(Modifier.width(80.dp).height(16.dp))
        }
    }
}
