package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.io.File
import java.io.ByteArrayInputStream
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

@OptIn(ExperimentalComposeUiApi::class)
class BottomNavigationTest {
    @Test
    fun selectedLabelUsesActionColorInBothThemesEvenWithTheLegacyMaterialMapping() {
        for (dark in listOf(false, true)) {
            val colors = sauceColors(dark)
            ImageComposeScene(width = 360, height = 160) {
                SauceTheme(darkTheme = dark, materialColors = colors.material.copy(primary = colors.surface, tertiary = colors.textPrimary)) {
                    Column { BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Review, true, {}) }
                }
            }.use { scene ->
                settle(scene)
                scene.saveFrame(if (dark) "dark" else "light")
                val labels = scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }
                for (label in labels) {
                    val result = mutableListOf<TextLayoutResult>()
                    label.config[SemanticsActions.GetTextLayoutResult].action?.invoke(result)
                    val layout = result.single()
                    assertEquals(if (layout.layoutInput.text.text == "Обзор") colors.action else colors.textSecondary,
                        layout.layoutInput.style.color)
                }
                assertEquals(4, labels.size)
            }
        }
    }

    @Test
    fun hiddenLabelsStillHaveAccessibleNamesAndEachTabSelectsItsOwnDestination() {
        val destinations = mutableListOf<BottomBarConfiguration>()
        navigationScene { BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Home, false, destinations::add) }.use { scene ->
            settle(scene)
            scene.saveFrame("icons-only")
            val tabs = scene.nodes().filter { it.config.contains(SemanticsProperties.Selected) }
            assertEquals(4, tabs.size)
            assertTrue(tabs.all { it.boundsInRoot.height >= 48f })
            assertEquals(listOf("Старты", "Обзор", "Слоты", "Профиль"), tabs.map {
                it.config.getOrNull(SemanticsProperties.ContentDescription)?.single()
            })
            tabs.forEach {
                scene.sendPointerEvent(PointerEventType.Press, it.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, it.boundsInRoot.center)
            }
            assertEquals(defaultMenuItems().map { it.bottomBarConfiguration }, destinations)
        }
    }

    @Test
    fun notificationsAreAnnouncedAndLongCountsAreCappedVisually() {
        val items = defaultMenuItems().mapIndexed { index, item ->
            item.copy(hasNews = index == 1, badgeCount = if (index == 0) 125 else 0)
        }
        navigationScene { BottomNavigation(items, BottomBarConfiguration.Home, true, {}) }.use { scene ->
            settle(scene)
            scene.saveFrame("notifications")
            val states = scene.nodes().mapNotNull { it.config.getOrNull(SemanticsProperties.StateDescription) }
            assertTrue("Уведомлений: 125" in states)
            assertTrue("Есть обновления" in states)
            // Badge text is intentionally hidden from semantics; its full count is announced above.
            val textLayouts = scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }
            assertEquals(4, textLayouts.size)
        }
    }

    @Test
    fun labelsAndTouchTargetsFitAtDoubleFontSize() {
        ImageComposeScene(width = 360, height = 220, density = Density(1f, 2f)) {
            SauceTheme { Column { BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Home, true, {}) } }
        }.use { scene ->
            settle(scene)
            scene.saveFrame("large-text")
            for (node in scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
                val result = mutableListOf<TextLayoutResult>()
                node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(result)
                val layout = result.single()
                assertTrue(!layout.hasVisualOverflow, "${layout.layoutInput.text.text}: size=${layout.size}, constraints=${layout.layoutInput.constraints}, lines=${layout.lineCount}, right=${layout.getLineRight(layout.lineCount - 1)}, width=${layout.didOverflowWidth}, height=${layout.didOverflowHeight}")
                if (layout.layoutInput.text.text == "Слоты") assertEquals(1, layout.lineCount)
            }
        }
    }

    @Test
    fun reselectingTheActiveTabRevealsItsHighlightFromTheCenterInBothLayouts() {
        for (wide in listOf(false, true)) {
            val colors = sauceColors(dark = false)
            val activations = mutableListOf<BottomBarConfiguration>()
            val items = defaultMenuItems()
            ImageComposeScene(width = 360, height = 480) {
                SauceTheme(darkTheme = false) {
                    if (wide) {
                        WideScreenNavigation(items, BottomBarConfiguration.Review, true, null,
                            activations::add, Modifier.size(112.dp, 480.dp))
                    } else {
                        Column(Modifier.background(colors.surface)) {
                            BottomNavigation(items, BottomBarConfiguration.Review, true, activations::add)
                        }
                    }
                }
            }.use { scene ->
                settle(scene)
                val tab = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Selected) == true }
                val bounds = tab.boundsInRoot
                val surface = colors.surface.toArgb()
                fun pixels(time: Long) = scene.render(time).use { frame ->
                    frame.encodeToData()!!.use { data ->
                        val image = ImageIO.read(ByteArrayInputStream(data.bytes))
                        val y = bounds.center.y.toInt()
                        image.getRGB(bounds.center.x.toInt() + 17, y) to image.getRGB(bounds.right.toInt() - 9, y)
                    }
                }

                var time = 1_400_000_000L
                val full = pixels(time)
                assertTrue(full.first != surface && full.second != surface)
                repeat(2) {
                    tab.config[SemanticsActions.OnClick].action!!.invoke()
                    val frames = List(20) { pixels(time.also { time += 16_000_000L }) }
                    assertTrue(frames.any { (center, edge) -> center != surface && edge == surface },
                        "The center must fill before the edge, wide=$wide")
                    assertEquals(full, pixels(time))
                }
                assertEquals(List<BottomBarConfiguration>(2) { BottomBarConfiguration.Review }, activations)
            }
        }
    }

    @Test
    fun profilePhotoFillsTheSameIconSlotAndClearsWhenTheAccountHasNoPhoto() {
        val file = File.createTempFile("sportsauce-dock-profile-", ".png")
        try {
            val pixels = BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB)
            for (x in 0 until 32) for (y in 0 until 32) pixels.setRGB(x, y, 0xFFFF2010.toInt())
            ImageIO.write(pixels, "png", file)
            var photo by mutableStateOf<String?>(null)
            navigationScene {
                BottomNavigation(defaultMenuItems(), BottomBarConfiguration.Profile, false, {}, profilePhotoUrl = photo)
            }.use { scene ->
                settle(scene)
                fun profile() = scene.nodes().single {
                    it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Профиль") == true
                }
                val bounds = profile().boundsInRoot
                var time = 1_500_000_000L
                fun profilePixel(): Int = scene.render(time.also { time += 40_000_000L }).use { image ->
                    image.encodeToData()!!.use { bytes ->
                        ImageIO.read(ByteArrayInputStream(bytes.bytes)).getRGB(bounds.center.x.toInt(), bounds.center.y.toInt())
                    }
                }
                fun Int.isPhotoRed() = (this shr 16 and 255) > 210 && (this shr 8 and 255) < 60
                assertTrue(!profilePixel().isPhotoRed())
                photo = file.toURI().toString()
                val loaded = (0 until 70).any {
                    val visible = profilePixel().isPhotoRed()
                    if (!visible) Thread.sleep(10)
                    visible
                }
                assertTrue(loaded)
                assertEquals(bounds, profile().boundsInRoot)
                photo = null
                assertTrue(!profilePixel().isPhotoRed())
                assertEquals(bounds, profile().boundsInRoot)
            }
        } finally {
            file.delete()
        }
    }

    private fun navigationScene(content: @Composable () -> Unit) =
        ImageComposeScene(width = 360, height = 160) { SauceTheme { Column { content() } } }

    private fun settle(scene: ImageComposeScene) {
        repeat(12) { scene.render(it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.saveFrame(name: String) {
        val output = File("build/design-system/bottom-$name.png").apply { parentFile.mkdirs() }
        render(1_500_000_000L).use { image ->
            checkNotNull(image.encodeToData()).use { output.writeBytes(it.bytes) }
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}
