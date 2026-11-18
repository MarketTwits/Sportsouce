package com.markettwits.core_ui.items.image

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import coil3.request.ImageRequest

expect fun ImageRequest.Builder.enableImageColorSampling(): ImageRequest.Builder

fun Painter.dominantImageColor(density: Density, direction: LayoutDirection, sampleHeight: Int = 18): Color {
    val sample = ImageBitmap(32, sampleHeight)
    val size = Size(sample.width.toFloat(), sample.height.toFloat())
    CanvasDrawScope().draw(density, direction, Canvas(sample), size) {
        draw(size)
    }

    // Group sampled pixels by hue so neutral areas and a few bright pixels do not wash out the tint.
    val pixels = sample.toPixelMap()
    val weights = FloatArray(12)
    val reds = FloatArray(weights.size)
    val greens = FloatArray(weights.size)
    val blues = FloatArray(weights.size)
    for (y in 0 until pixels.height) {
        for (x in 0 until pixels.width) {
            val color = pixels[x, y]
            val brightest = maxOf(color.red, color.green, color.blue)
            val chroma = brightest - minOf(color.red, color.green, color.blue)
            if (chroma < 0.06f || color.alpha < 0.5f) continue

            val hue = when (brightest) {
                color.red -> (color.green - color.blue) / chroma
                color.green -> 2f + (color.blue - color.red) / chroma
                else -> 4f + (color.red - color.green) / chroma
            }
            val bin = ((hue + 6f) * 2f + 0.5f).toInt() % weights.size
            val weight = chroma * color.alpha
            weights[bin] += weight
            reds[bin] += color.red * weight
            greens[bin] += color.green * weight
            blues[bin] += color.blue * weight
        }
    }
    val dominant = weights.indices.maxBy { weights[it] }
    val weight = weights[dominant]
    if (weight == 0f) return Color.Unspecified
    return Color(
        red = (reds[dominant] / weight).coerceIn(0f, 1f),
        green = (greens[dominant] / weight).coerceIn(0f, 1f),
        blue = (blues[dominant] / weight).coerceIn(0f, 1f),
    )
}
