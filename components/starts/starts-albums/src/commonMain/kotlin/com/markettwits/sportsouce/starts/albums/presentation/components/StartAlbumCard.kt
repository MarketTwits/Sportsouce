package com.markettwits.sportsouce.starts.albums.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.sportsouce.starts.albums.domain.StartAlbumsItem

private const val COVER_ASPECT_RATIO = 16f / 11f

@Composable
internal fun StartAlbumCard(
    album: StartAlbumsItem.Album,
    modifier: Modifier = Modifier,
    start: StartAlbumsItem? = null,
    onClick: () -> Unit,
) {
    OnBackgroundCard(modifier = modifier, onClick = onClick) {
        AlbumImage(album.previewUrl, AlbumImageSize.Cover, Modifier.fillMaxWidth().aspectRatio(COVER_ASPECT_RATIO))
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = start?.name ?: album.name,
                style = MaterialTheme.typography.titleSmall,
                color = SauceTheme.colors.textPrimary,
                minLines = if (start != null) 2 else 1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (start != null) {
                val details = listOfNotNull(
                    album.name.takeUnless { it.isBlank() || it.equals(start.name, ignoreCase = true) },
                    start.date.takeIf { it.isNotBlank() },
                ).joinToString(" · ")
                Text(details, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(18.dp), tint = SauceTheme.colors.textMuted)
                Text("${album.photosCount} фото", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
            }
        }
    }
}

@Composable
internal fun StartAlbumShimmer(showStartInCard: Boolean) {
    val typography = MaterialTheme.typography
    val density = LocalDensity.current
    val titleHeight = with(density) { typography.titleSmall.lineHeight.toDp() }
    val detailsHeight = with(density) { typography.bodySmall.lineHeight.toDp() }
    OnBackgroundCard {
        SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(COVER_ASPECT_RATIO))
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SauceSkeleton(Modifier.fillMaxWidth(.7f).height(titleHeight * if (showStartInCard) 2 else 1))
            if (showStartInCard) SauceSkeleton(Modifier.fillMaxWidth(.5f).height(detailsHeight))
            SauceSkeleton(Modifier.width(80.dp).height(maxOf(18.dp, detailsHeight)))
        }
    }
}
