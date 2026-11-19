package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import kotlin.math.max
import kotlin.math.min

/** Window coordinates keep the image aligned between the album and the fullscreen overlay. */
data class PhotoTransitionSource(val bounds: Rect, val painter: Painter, val cornerRadiusPx: Float = 0f)

internal data class PhotoTransitionGeometry(val clip: Rect, val image: Rect, val cornerRadius: Float)

/** Interpolate the crop as well as its bounds: a square thumbnail reveals the original aspect ratio. */
internal fun photoTransitionGeometry(source: Rect, imageSize: Size, viewport: Size, progress: Float, corner: Float, viewerOffset: Offset = Offset.Zero, viewerScale: Float = 1f): PhotoTransitionGeometry {
    val p = progress.coerceIn(0f, 1f)
    val cover = max(source.width / imageSize.width, source.height / imageSize.height)
    val fit = min(viewport.width / imageSize.width, viewport.height / imageSize.height) * viewerScale
    fun centered(center: Offset, scale: Float) = Rect(center - Offset(imageSize.width * scale / 2, imageSize.height * scale / 2), imageSize * scale)
    val crop = centered(source.center, cover)
    val destination = centered(Offset(viewport.width / 2, viewport.height / 2) + viewerOffset, fit)
    return PhotoTransitionGeometry(lerp(source, destination, p), lerp(crop, destination, p), corner * (1 - p))
}

@Composable
internal fun PhotoSharedTransition(source: PhotoTransitionSource, progress: Float, windowOrigin: Offset, modifier: Modifier = Modifier, viewerOffset: Offset = Offset.Zero, viewerScale: Float = 1f) {
    Canvas(modifier) {
        val intrinsic = source.painter.intrinsicSize
        if (!intrinsic.width.isFinite() || !intrinsic.height.isFinite() || intrinsic.width <= 0 || intrinsic.height <= 0) return@Canvas
        val geometry = photoTransitionGeometry(source.bounds.translate(-windowOrigin), intrinsic, size, progress, source.cornerRadiusPx, viewerOffset, viewerScale)
        val path = Path().apply { addRoundRect(RoundRect(geometry.clip, CornerRadius(geometry.cornerRadius))) }
        clipPath(path) {
            translate(geometry.image.left, geometry.image.top) {
                with(source.painter) { draw(geometry.image.size) }
            }
        }
    }
}
