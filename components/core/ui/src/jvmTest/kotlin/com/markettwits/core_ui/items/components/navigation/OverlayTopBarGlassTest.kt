package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class OverlayTopBarGlassTest {
    @Test fun openingKeepsTheSurfaceCoveredUntilTheBackdropIsRecorded() {
        for (dark in listOf(false, true)) {
            var sourceAttached by mutableStateOf(false)
            val fallback = sauceColors(dark).surface.toArgb()
            val rawColor = if (dark) Color.Yellow else Color.Red
            ImageComposeScene(width = 390, height = 250) {
                SauceTheme(darkTheme = dark) {
                    SauceTopBarOverlayHost {
                        Box(Modifier.fillMaxSize().background(rawColor)) {
                            if (sourceAttached) {
                                SauceTopBarBackdrop(Modifier.fillMaxSize()) {
                                    Box(Modifier.fillMaxSize().background(rawColor))
                                }
                            }
                            SauceTopBarOverlay(Modifier.fillMaxWidth().height(56.dp)) {
                                Box(Modifier.align(Alignment.Center).size(20.dp).background(Color.Green))
                            }
                        }
                    }
                }
            }.use { scene ->
                var time = 1_000_000_000L
                fun frame() = scene.render((time + 16_666_667L).also { time = it }).use { image ->
                    image.encodeToData()!!.use { ImageIO.read(ByteArrayInputStream(it.bytes)) }
                }
                fun distance(a: Int, b: Int) =
                    listOf(0, 8, 16).sumOf { kotlin.math.abs((a shr it and 255) - (b shr it and 255)) }
                assertEquals(fallback, frame().getRGB(370, 28), "First frame must cover the previous screen")
                sourceAttached = true
                val frames = List(20) {
                    val image = frame()
                    assertEquals(Color.Green.toArgb(), image.getRGB(195, 28), "Controls must stay sharp")
                    distance(fallback, image.getRGB(370, 28))
                }
                assertTrue(frames.last() > 20, "Live glass must become visible: dark=$dark frames=$frames")
                assertTrue(frames.any { it in 1 until frames.last() - 3 }, "Glass must reveal across multiple frames")
                assertTrue(frames.max() <= frames.last() + 8, "The first frames must never expose the raw backdrop")
                sourceAttached = false
                assertEquals(fallback, frame().getRGB(370, 28), "Removing a source must restore the surface in the same frame")
            }
        }
    }

    @Test fun floatingToolbarUsesTheSharedLiveGlassAndKeepsControlsOutOfItsBackdrop() {
        for (dark in listOf(false, true)) {
            var source by mutableStateOf(Color.Red)
            var marker by mutableStateOf(Color.Green)
            var opacity by mutableFloatStateOf(1f)
            ImageComposeScene(width = 390, height = 700) {
                SauceTheme(darkTheme = dark) {
                    SauceTopBarOverlayHost {
                        Box(Modifier.fillMaxSize()) {
                            SauceTopBarBackdrop(Modifier.fillMaxSize()) { Box(Modifier.fillMaxSize().background(source)) }
                            SauceTopBarOverlay(Modifier.fillMaxWidth().height(56.dp), glassAlpha = { opacity }) {
                                Text("Toolbar", Modifier.align(Alignment.CenterStart))
                                Box(Modifier.align(Alignment.Center).size(20.dp).background(marker))
                            }
                        }
                    }
                }
            }.use { scene ->
                var time = System.nanoTime()
                fun settle() { repeat(12) { time += 100_000_000L; scene.render(time).close() } }
                fun pixel(x: Int = 370, y: Int = 28): Int = scene.render(time).use { img ->
                    img.encodeToData()!!.use { data -> ImageIO.read(ByteArrayInputStream(data.bytes)).getRGB(x, y) }
                }
                settle(); val red = pixel()
                source = Color.Blue; settle(); val blue = pixel()
                fun difference(a: Int, b: Int) = listOf(0, 8, 16).sumOf { kotlin.math.abs((a shr it and 255) - (b shr it and 255)) }
                assertTrue(difference(red, blue) > 30, "Glass must sample live content instead of a flat surface")
                assertEquals(Color.Green.toArgb(), pixel(195, 28), "Toolbar controls must remain sharp")
                marker = Color.Yellow; settle()
                assertTrue(difference(blue, pixel()) < 8, "Controls must never feed back into the glass")
                opacity = 0f; settle()
                assertEquals(Color.Blue.toArgb(), pixel(), "An expanded photo cover must show through the toolbar without glass tint")
            }
        }
    }
}
