package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.topbar.SauceTopBarOverlay
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumDetails

/** The cover is part of the grid's scroll, rather than a second independently scrolling surface. */
internal fun albumCoverScrollOffset(grid: LazyGridState, extent: Float): Float =
    if (grid.firstVisibleItemIndex == 0) grid.firstVisibleItemScrollOffset.toFloat().coerceIn(0f, extent)
    else extent

internal fun albumCoverContentAlpha(progress: Float) = (1f - progress / .6f).coerceIn(0f, 1f)
internal fun albumCoverPinnedAlpha(progress: Float) = ((progress - .6f) / .4f).coerceIn(0f, 1f)
internal fun albumCoverTitleAlpha(progress: Float) = if (albumCoverContentAlpha(progress) == 0f) 1f else 0f

@Composable
internal fun AlbumCoverBackground(url: String, height: Dp, offset: () -> Float, progress: () -> Float) {
    val background = SauceTheme.colors.background
    Box(Modifier.fillMaxWidth().height(height).graphicsLayer {
        translationY = -offset()
        alpha = 1f - albumCoverPinnedAlpha(progress())
    }.clipToBounds().background(SauceTheme.colors.surfaceSubtle)) {
        if (url.isNotBlank()) AlbumImage(url, AlbumImageSize.Cover, Modifier.fillMaxSize(), error = { _, _ -> })
        // The middle scrim supports long titles; the bottom dissolves into the photo grid.
        Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = .24f)))
        Box(Modifier.matchParentSize().background(Brush.verticalGradient(
            0f to Color.Black.copy(alpha = .3f), .28f to Color.Transparent, 1f to Color.Transparent)))
        Box(Modifier.align(Alignment.Center).fillMaxWidth().fillMaxHeight(.55f)
            .background(Brush.radialGradient(listOf(Color.Black.copy(alpha = .25f), Color.Transparent))))
        Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(100.dp)
            .background(Brush.verticalGradient(listOf(background.copy(alpha = 0f), background))))
    }
}

@Composable
internal fun AlbumCoverCaption(album: StartAlbumDetails, height: Dp, offset: () -> Float, progress: () -> Float, loading: Boolean = false, stretch: () -> Float = { 0f }) {
    Box(Modifier.fillMaxWidth().height(height).graphicsLayer {
        // Content moves half as fast as the cover while it fades out, as in Ente's cover header.
        translationY = offset() * .5f - stretch() * .5f
        alpha = albumCoverContentAlpha(progress())
    }.semantics { if (albumCoverContentAlpha(progress()) == 0f) hideFromAccessibility() },
        contentAlignment = Alignment.Center) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (loading) {
                SauceSkeleton(Modifier.width(140.dp).height(28.dp), shape = CircleShape)
                SauceSkeleton(Modifier.fillMaxWidth(.75f).height(40.dp))
                SauceSkeleton(Modifier.fillMaxWidth(.5f).height(18.dp))
            } else {
                if (album.date.isNotBlank()) Surface(color = Color.Black.copy(alpha = .32f), shape = CircleShape) {
                    Text(album.date, Modifier.padding(horizontal = 14.dp, vertical = 7.dp), color = Color.White,
                        style = MaterialTheme.typography.labelMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(album.name, Modifier.fillMaxWidth().semantics { heading() }, style = MaterialTheme.typography.headlineLarge,
                    color = Color.White, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (album.startName.isNotBlank() && album.startName != album.name) {
                    Text(album.startName, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = .92f), textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Text("${album.photosCount} фото", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = .85f))
            }
        }
    }
}

@Composable
internal fun AlbumCoverToolbar(title: String, progress: () -> Float, hasCover: Boolean, goBack: () -> Unit, onShare: (() -> Unit)? = null) {
    val colors = SauceTheme.colors
    val pinned = if (hasCover) albumCoverPinnedAlpha(progress()) else 1f
    val foreground = if (hasCover) lerp(Color.White, colors.textPrimary, ((pinned - .4f) / .2f).coerceIn(0f, 1f)) else colors.textPrimary
    SauceTopBarOverlay(glassAlpha = { pinned }) {
        Row(Modifier.fillMaxWidth().statusBarsPadding()
            .heightIn(min = 56.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = goBack, modifier = Modifier.size(48.dp), shape = CircleShape) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = foreground)
            }
            val titleAlpha = if (hasCover) albumCoverTitleAlpha(progress()) else 1f
            Text(title, Modifier.weight(1f).padding(horizontal = 8.dp).graphicsLayer {
                alpha = titleAlpha
            }.semantics { if (titleAlpha < .5f) hideFromAccessibility() },
                style = MaterialTheme.typography.titleMedium, color = foreground, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (onShare != null) IconButton(onClick = onShare, modifier = Modifier.size(48.dp), shape = CircleShape) {
                Icon(Icons.Outlined.Share, "Поделиться альбомом", tint = foreground)
            }
        }
    }
}
