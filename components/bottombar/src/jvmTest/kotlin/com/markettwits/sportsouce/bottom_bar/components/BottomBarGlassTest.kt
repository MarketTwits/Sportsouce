package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalStaggeredGrid
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.Text
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class BottomBarGlassTest {
    @Test
    fun desktopRailLeavesRoomForTheScreenAndRestoresAfterFullscreenUpdate() {
        var visible by mutableStateOf(true)
        ImageComposeScene(width = 1280, height = 860) {
            SauceTheme {
                BottomBarContent(isShowTopBar = visible, isShowLabel = true, items = defaultMenuItems(),
                    selectedTab = BottomBarConfiguration.Review, onClickTab = {}) {
                    Box(Modifier.fillMaxSize()) { Text("DESKTOP_SCREEN", Modifier.fillMaxSize()) }
                }
            }
        }.use { scene ->
            scene.settle()
            val before = scene.text("DESKTOP_SCREEN").boundsInRoot
            assertTrue(before.width >= 1100f, "Rail must leave the desktop screen visible: $before")
            assertTrue(before.left in 80f..180f)
            assertTrue(scene.tabs().all { it.boundsInRoot.width < 180f })
            visible = false; scene.settle()
            assertEquals(1280f, scene.text("DESKTOP_SCREEN").boundsInRoot.width)
            visible = true; scene.settle()
            assertEquals(before, scene.text("DESKTOP_SCREEN").boundsInRoot)
            scene.save("desktop-navigation-restored")
        }
    }

    @Test
    fun allVerticalViewportsFeedLivePixelsThroughClippingAncestors() {
        for (kind in 0..3) for (dark in listOf(false, true)) {
            var backdrop by mutableStateOf(Color(0xFFE26C48))
            ImageComposeScene(width = 390, height = 844) {
                SauceTheme(darkTheme = dark) {
                    BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                        selectedTab = BottomBarConfiguration.Home, onClickTab = {}) {
                        Box(Modifier.fillMaxSize().clip(RectangleShape)) {
                            val block: @Composable () -> Unit = {
                                Box(Modifier.fillMaxWidth().height(160.dp).background(backdrop))
                            }
                            when (kind) {
                                0 -> Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                    repeat(30) { block() }
                                }
                                1 -> SauceLazyColumn(Modifier.fillMaxSize()) { items(30) { block() } }
                                2 -> SauceLazyVerticalGrid(GridCells.Fixed(2), Modifier.fillMaxSize()) { items(60) { block() } }
                                3 -> SauceLazyVerticalStaggeredGrid(StaggeredGridCells.Fixed(2), Modifier.fillMaxSize()) { items(60) { block() } }
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val y = (scene.tabs().first().boundsInRoot.top + 25f).toInt()
                val before = scene.bitmap().getRGB(6, y)
                backdrop = Color(0xFF4386E2); scene.settle()
                assertTrue(rgbDifference(before, scene.bitmap().getRGB(6, y)) > 8,
                    "Clip on an ancestor must not remove live backdrop: kind=$kind dark=$dark")
                scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 300f)
                scene.settle()
                backdrop = Color(0xFFE26C48); scene.settle()
                assertTrue(rgbDifference(scene.bitmap().getRGB(6, y), before) < 8,
                    "Backdrop must keep updating after scrolling: kind=$kind dark=$dark")
            }
        }
    }

    @Test
    fun nestedScaffoldDrawsLiveScrollingPixelsUnderGlassWithoutScreenOptIn() {
        var backdrop by mutableStateOf(Color(0xFFE26C48))
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme {
                BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                    selectedTab = BottomBarConfiguration.Home, onClickTab = {}) {
                    Box(Modifier.fillMaxSize().graphicsLayer { alpha = 1f }) {
                        Scaffold(topBar = { Text("Nested screen", Modifier.height(64.dp)) }) { padding ->
                            Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)
                                .verticalScroll(rememberScrollState()).background(backdrop)) {
                                Spacer(Modifier.height(1800.dp))
                                Text("NESTED_LAST")
                            }
                        }
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val y = (scene.tabs().first().boundsInRoot.top + 25f).toInt()
            val before = scene.bitmap().getRGB(6, y)
            backdrop = Color(0xFF4386E2); scene.settle()
            assertTrue(rgbDifference(before, scene.bitmap().getRGB(6, y)) > 8,
                "Nested Scaffold must not clip away the background sampled by root glass")
            scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
            scene.settle()
            assertTrue(scene.text("NESTED_LAST").boundsInRoot.bottom <= scene.tabs().first().boundsInRoot.top)
            scene.save("root-glass-nested-scaffold")
        }
    }

    @Test
    fun rootGlassSamplesLiveContentWhileLabelsAndDestinationsStayIntact() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var backdrop by mutableStateOf(Color(0xFFE26C48))
            val destinations = mutableListOf<BottomBarConfiguration>()
            ImageComposeScene(width = 390, height = 844, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                        selectedTab = BottomBarConfiguration.Home, onClickTab = destinations::add) {
                        // The real screen stacks contain graphics layers for Decompose transitions.
                        Box(Modifier.fillMaxSize().graphicsLayer { alpha = 1f }) {
                            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(backdrop)) {
                                Spacer(Modifier.height(1800.dp))
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val firstBounds = scene.tabs().map { it.boundsInRoot }
                val first = scene.bitmap()
                backdrop = Color(0xFF4386E2); scene.settle()
                val second = scene.bitmap()
                val y = (firstBounds.first().top + 25f).toInt()
                val a = first.getRGB(6, y); val b = second.getRGB(6, y)
                assertTrue(rgbDifference(a, b) > 8, "Glass must update from actual background pixels: dark=$dark scale=$scale")
                assertEquals(firstBounds, scene.tabs().map { it.boundsInRoot })
                assertTrue(scene.tabs().all { it.boundsInRoot.height >= 48f })
                scene.assertLabelsFit()
                scene.tabs().forEach {
                    scene.sendPointerEvent(PointerEventType.Press, it.boundsInRoot.center)
                    scene.sendPointerEvent(PointerEventType.Release, it.boundsInRoot.center)
                }
                assertEquals(defaultMenuItems().map { it.bottomBarConfiguration }, destinations)
                scene.settle()
                scene.save("root-glass-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun fineBackgroundDetailIsBlurredAndFallbackPreservesNavigationGeometry() {
        for (dark in listOf(false, true)) {
            var enabled by mutableStateOf(true)
            ImageComposeScene(width = 390, height = 220) {
                SauceTheme(darkTheme = dark) {
                    val haze = remember { HazeState() }.apply { blurEnabled = enabled }
                    Box(Modifier.fillMaxSize()) {
                        Canvas(Modifier.fillMaxSize().hazeSource(haze)) {
                            repeat(195) { i -> drawRect(if (i % 2 == 0) Color.Black else Color.White,
                                topLeft = Offset(i * 2f, 0f), size = Size(2f, size.height)) }
                        }
                        Box(Modifier.align(Alignment.BottomCenter)) {
                            GlassBottomNavigation(haze, defaultMenuItems(), BottomBarConfiguration.Review, true, {})
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val bounds = scene.tabs().map { it.boundsInRoot }
                val image = scene.bitmap()
                val y = (bounds.first().top + 25f).toInt()
                val values = (4..16).map { image.getRGB(it, y) and 255 }
                assertTrue(values.max() - values.min() < 8, "Fine stripes should disappear under strong blur: $values")
                assertTrue(rgbDifference(image.getRGB(4, 20), image.getRGB(6, 20)) > 700,
                    "The content layer itself must remain sharp")
                enabled = false; scene.settle()
                assertEquals(bounds, scene.tabs().map { it.boundsInRoot })
                scene.assertLabelsFit()
                scene.save("root-glass-fallback-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test
    fun scrollEndClearsTheBarAndOrdinaryScreensRetainTheirSafeLayoutWhenVisibilityChanges() {
        var visible by mutableStateOf(true)
        var underlay by mutableStateOf(true)
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme {
                BottomBarContent(isShowTopBar = visible, isShowLabel = true, items = defaultMenuItems(),
                    selectedTab = BottomBarConfiguration.Home, onClickTab = {}) {
                    if (underlay) Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(1800.dp))
                        Text("LAST_ITEM")
                    } else Box(Modifier.fillMaxSize()) {
                        Text("PINNED_ACTION", Modifier.align(Alignment.BottomCenter))
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val scroll = scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
            assertEquals(844f, scroll.boundsInRoot.bottom, "Opted-in viewport must extend under the bar")
            scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f); scene.settle()
            val last = scene.text("LAST_ITEM")
            assertTrue(last.boundsInRoot.height > 0f)
            assertTrue(last.boundsInRoot.bottom <= scene.tabs().first().boundsInRoot.top)
            underlay = false; scene.settle()
            assertTrue(scene.text("PINNED_ACTION").boundsInRoot.bottom <= scene.tabs().first().boundsInRoot.top)
            visible = false; scene.settle()
            assertTrue(scene.tabs().isEmpty())
            assertEquals(844f, scene.text("PINNED_ACTION").boundsInRoot.bottom)
            visible = true; scene.settle()
            assertEquals(3, scene.tabs().size)
        }
    }

    @Test
    fun notificationAnimationKeepsTheViewportStableAndItsBackdropVisible() {
        for (dark in listOf(false, true)) {
            var visible by mutableStateOf(false)
            var contentHeight = 0
            var notificationClicks = 0
            val backdrop = Color(0xFF4386E2)
            ImageComposeScene(width = 390, height = 844) {
                SauceTheme(darkTheme = dark) {
                    BottomBarContent(
                        isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                        selectedTab = BottomBarConfiguration.Review, onClickTab = {},
                        aboveNavigation = {
                            AnimatedVisibility(visible,
                                enter = expandVertically(animationSpec = tween(280)),
                                exit = shrinkVertically(animationSpec = tween(220))) {
                                Box(Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 16.dp)
                                    .background(Color.Green).clickable { notificationClicks++ })
                            }
                        },
                        content = {
                            Box(Modifier.fillMaxSize().onSizeChanged { contentHeight = it.height }) {
                                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(backdrop)) {
                                    Spacer(Modifier.height(1800.dp))
                                    Text("NOTIFICATION_LAST_ITEM")
                                }
                            }
                        },
                    )
                }
            }.use { scene ->
                scene.settle()
                val heightBefore = contentHeight
                val tabsBefore = scene.tabs().map { it.boundsInRoot }
                val sampleY = tabsBefore.first().top.toInt() - 10
                for (show in listOf(true, false, true)) {
                    visible = show
                    repeat(24) { frame ->
                        time += 16_000_000L
                        scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                            val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                            assertEquals(heightBefore, contentHeight,
                                "Notification must not resize the screen: dark=$dark show=$show frame=$frame")
                            assertEquals(tabsBefore, scene.tabs().map { it.boundsInRoot })
                            assertTrue(rgbDifference(bitmap.getRGB(6, sampleY), 0xFF4386E2.toInt()) < 8,
                                "Transparent notice margins must retain content pixels throughout the animation")
                        } }
                    }
                }
                val noticeCenter = Offset(195f, tabsBefore.first().top - 24f)
                scene.sendPointerEvent(PointerEventType.Press, noticeCenter)
                scene.sendPointerEvent(PointerEventType.Release, noticeCenter)
                assertEquals(1, notificationClicks, "The overlay must retain its touch target")
                scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
                assertTrue(scene.text("NOTIFICATION_LAST_ITEM").boundsInRoot.bottom <= tabsBefore.first().top - 48f,
                    "The last item must scroll clear of both notification and navigation")
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(15) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.bitmap() = render(time.also { time += 100_000_000L }).use { image ->
        image.encodeToData()!!.use { ImageIO.read(ByteArrayInputStream(it.bytes)) }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.tabs() = nodes().filter { it.config.contains(SemanticsProperties.Selected) }
    private fun ImageComposeScene.text(label: String) = nodes().single {
        it.config.getOrNull(SemanticsProperties.Text)?.any { text -> text.text == label } == true
    }
    private fun ImageComposeScene.assertLabelsFit() {
        nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }.forEach { node ->
            val layouts = mutableListOf<TextLayoutResult>()
            node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
            assertFalse(layouts.single().hasVisualOverflow)
        }
    }
    private fun rgbDifference(a: Int, b: Int) = listOf(0, 8, 16).sumOf { abs(((a shr it) and 255) - ((b shr it) and 255)) }
}
