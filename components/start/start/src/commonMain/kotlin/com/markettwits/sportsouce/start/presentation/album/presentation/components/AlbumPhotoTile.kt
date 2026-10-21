package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.PhotoTransitionSource
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize

internal val AlbumPhotoShape = RoundedCornerShape(2.dp)

/** One layout: three columns on phones, larger column counts on tablets and desktop. */
internal object AlbumGridCells : GridCells {
    override fun Density.calculateCrossAxisCellSizes(availableSize: Int, spacing: Int): List<Int> {
        val width = availableSize.toDp()
        val columns = if (width < 600.dp) 3 else (width / 160.dp).toInt().coerceAtLeast(3)
        val space = (availableSize - spacing * (columns - 1)).coerceAtLeast(0)
        return List(columns) { space / columns + if (it < space % columns) 1 else 0 }
    }
}

/** Only mounted tiles retain painters; a large album does not retain every decoded image. */
internal class AlbumPhotoSources {
    private val sources = mutableMapOf<Int, PhotoTransitionSource>()
    private val bounds = mutableMapOf<Int, Rect>()
    operator fun get(index: Int) = sources[index]
    fun boundsAt(index: Int): Rect? = bounds[index]
    fun indexAt(position: Offset): Int? = bounds.entries.firstOrNull { it.value.contains(position) }?.key
    fun update(index: Int, bounds: Rect?, painter: Painter?, radius: Float) {
        if (bounds != null) this.bounds[index] = bounds
        if (bounds != null && painter != null) sources[index] = PhotoTransitionSource(bounds, painter, radius)
    }
    fun remove(index: Int) { sources.remove(index); bounds.remove(index) }
}

@Composable
internal fun AlbumPhotoTile(index: Int, url: String, label: String, sources: AlbumPhotoSources,
    selectionMode: Boolean = false, selected: Boolean = false, onLongClick: (() -> Unit)? = null, onClick: () -> Unit) {
    val radius = with(LocalDensity.current) { 2.dp.toPx() }
    val registration = remember(index, url, sources, radius) { PhotoTileRegistration(index, sources, radius) }
    DisposableEffect(registration) { onDispose { sources.remove(index) } }
    Box(Modifier.fillMaxWidth().aspectRatio(1f)
        .onGloballyPositioned {
            // Keep the complete cell, including a partially clipped row at the viewport edge.
            registration.bounds = Rect(it.localToWindow(Offset.Zero), Size(it.size.width.toFloat(), it.size.height.toFloat()))
            registration.update()
        }.clip(AlbumPhotoShape).clickable(role = Role.Button, onClick = onClick)
        .semantics {
            contentDescription = label
            if (selectionMode) this.selected = selected
            onLongClick?.let { action -> onLongClick(label = "Выбрать фото") { action(); true } }
        }) {
        AlbumImage(url, AlbumImageSize.Grid, Modifier.fillMaxSize().graphicsLayer {
            scaleX = if (selected) .91f else 1f; scaleY = scaleX
        }.clip(AlbumPhotoShape), onPainter = { registration.painter = it; registration.update() })
        if (selectionMode) {
            if (selected) Box(Modifier.matchParentSize().border(2.dp, SauceTheme.colors.action, AlbumPhotoShape))
            Icon(if (selected) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                null, Modifier.align(Alignment.TopEnd).padding(6.dp).size(24.dp)
                    .background(SauceTheme.colors.surface.copy(alpha = .9f), androidx.compose.foundation.shape.CircleShape),
                tint = if (selected) SauceTheme.colors.action else SauceTheme.colors.textMuted)
        }
    }
}

/** Geometry changes while scrolling do not invalidate the composition of every visible cell. */
private class PhotoTileRegistration(val index: Int, val sources: AlbumPhotoSources, val radius: Float) {
    var bounds: Rect? = null
    var painter: Painter? = null
    fun update() { sources.update(index, bounds, painter, radius) }
}
