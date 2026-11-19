package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.use
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class PhotoViewerBackdropTest {
    @Test
    fun glassPreservesAlbumColorsWhileBlurringDetailsAndKeepingForegroundSharp() {
        var progress by mutableFloatStateOf(0f)
        var blue by mutableStateOf(false)
        var time = System.nanoTime()
        ImageComposeScene(width = 200, height = 300) {
            SauceTheme(darkTheme = true) {
                val haze = rememberHazeState()
                Box(Modifier.fillMaxSize()) {
                    Canvas(Modifier.fillMaxSize().hazeSource(haze)) {
                        for (x in 0 until 200 step 2) {
                            val color = if (blue) {
                                if (x % 4 == 0) Color(0xFF4080FF) else Color(0xFF8050FF)
                            } else {
                                if (x % 4 == 0) Color(0xFFFF5040) else Color(0xFFFFC040)
                            }
                            drawRect(color, Offset(x.toFloat(), 0f), Size(2f, size.height))
                        }
                    }
                    PhotoViewerBackdropLayer(haze, progress, Modifier.matchParentSize())
                    Box(Modifier.offset(10.dp, 200.dp).size(20.dp).background(Color.Green))
                }
            }
        }.use { scene ->
            fun pixels(): androidx.compose.ui.graphics.PixelMap {
                repeat(8) { time += 40_000_000; scene.render(time).close() }
                return scene.render(time + 1).use { it.toComposeImageBitmap().toPixelMap() }
            }
            val original = pixels()
            assertEquals(Color(0xFFFF5040), original[60, 100], "Opening must begin with the untouched album")
            progress = 1f
            val blurred = pixels()
            val details = (60..80).map { blurred[it, 100].green }
            assertTrue(details.max() - details.min() < .02f, "Fine album details must disappear under strong blur")
            assertEquals(Color.Green, blurred[15, 205], "The foreground must stay sharp and unfiltered")
            blue = true
            val updated = pixels()
            assertTrue(blurred[70, 100].red - updated[70, 100].red > .04f, "The material must sample the actual album rather than an opaque background")
            assertTrue(updated[70, 100].blue - blurred[70, 100].blue > .04f)
            assertEquals(Color.Green, updated[15, 205])
        }
    }
}
