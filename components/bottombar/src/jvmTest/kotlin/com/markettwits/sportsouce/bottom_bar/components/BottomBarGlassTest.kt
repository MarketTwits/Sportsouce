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
import com.markettwits.core_ui.items.components.navigation.excludeReservedBottomInset
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
    fun desktopRailSpacesAndRoundsTheTabs() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var visible by mutableStateOf(true)
            var selected by mutableStateOf<BottomBarConfiguration>(BottomBarConfiguration.Review)
            val height = if (scale > 1f) 640 else 860
            ImageComposeScene(width = 1280, height = height, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    BottomBarContent(isShowTopBar = visible, isShowLabel = true, items = defaultMenuItems(),
                        selectedTab = selected, onClickTab = { selected = it }) {
                        Box(Modifier.fillMaxSize()) { Text("DESKTOP_SCREEN", Modifier.fillMaxSize()) }
                    }
                }
            }.use { scene ->
                scene.settle()
                val before = scene.text("DESKTOP_SCREEN").boundsInRoot
                val tabs = scene.tabs()
                assertTrue(before.width >= 1100f, "Rail must leave the desktop screen visible: $before")
                assertTrue(before.left in 100f..160f)
                assertEquals(4, tabs.size)
                assertTrue(tabs.all {
                    it.boundsInRoot.height >= 84f && it.boundsInRoot.left >= 8f && it.boundsInRoot.right <= before.left - 8f
                })
                assertTrue(tabs.zipWithNext().all { (a, b) -> b.boundsInRoot.top - a.boundsInRoot.bottom >= 8f })
                assertTrue(scene.nodes().none {
                    it.config.getOrNull(SemanticsProperties.ContentDescription) == listOf("Спорт Союз")
                })
                val active = tabs.single { it.config[SemanticsProperties.Selected] }.boundsInRoot
                val image = scene.bitmap()
                val x = active.left.toInt()
                val y = active.top.toInt()
                val railColor = image.getRGB(x - 2, y + 1)
                assertEquals(railColor, image.getRGB(x + 1, y + 1), "The corner must follow the item shape")
                assertNotEquals(railColor, image.getRGB(x + 18, y + 4), "The selection fill must stay visible")
                scene.save("wide-navigation-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                scene.assertLabelsFit()
                tabs.last().config[SemanticsActions.OnClick].action!!.invoke()
                scene.settle()
                assertEquals(BottomBarConfiguration.Profile, selected)
                assertTrue(scene.tabs().last().config[SemanticsProperties.Selected])
                visible = false
                scene.settle()
                assertEquals(1280f, scene.text("DESKTOP_SCREEN").boundsInRoot.width)
                visible = true
                scene.settle()
                assertEquals(before, scene.text("DESKTOP_SCREEN").boundsInRoot)
            }
        }
    }

    @Test
    fun wideScreensKeepTheirLastItemAndFixedFooterAboveTheTaskbar() {
        val taskbar = WindowInsets(bottom = 56.dp)
        var railVisible by mutableStateOf(true)
        ImageComposeScene(width = 1280, height = 780) {
            SauceTheme {
                BottomBarContent(
                    isShowTopBar = railVisible,
                    isShowLabel = true,
                    items = defaultMenuItems(),
                    selectedTab = BottomBarConfiguration.Review,
                    onClickTab = {},
                    systemBottomInsets = taskbar,
                ) {
                    Scaffold(bottomBar = {
                        Box(Modifier.fillMaxWidth().windowInsetsPadding(taskbar)) {
                            Text("FIXED", Modifier.height(48.dp))
                        }
                    }) { padding ->
                        SauceLazyColumn(
                            modifier = Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding),
                            contentPadding = taskbar.excludeReservedBottomInset().asPaddingValues(),
                        ) {
                            items(20) { Text("ITEM_$it", Modifier.height(84.dp)) }
                        }
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val viewport = scene.nodes().single {
                it.config.contains(SemanticsActions.ScrollBy) && it.config.contains(SemanticsProperties.CollectionInfo)
            }
            viewport.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10_000f)
            scene.settle()
            for (showRail in listOf(true, false)) {
                railVisible = showRail
                scene.settle()
                val footer = scene.text("FIXED").boundsInRoot
                assertEquals(724f, footer.bottom)
                assertEquals(footer.top, scene.text("ITEM_19").boundsInRoot.bottom,
                    "The last item should reach the footer without adding a second taskbar inset")
            }
        }
    }

    @Test
    fun allVerticalViewportsDrawThroughTheDockMarginsInsideClippingAncestors() {
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
                val y = (scene.tabs().first().boundsInRoot.top - 3f).toInt()
                val before = scene.bitmap().getRGB(195, y)
                assertTrue(rgbDifference(scene.bitmap().getRGB(6, y), backdrop.toArgb()) < 8,
                    "A nested screen must fill the margins beside the dock: kind=$kind dark=$dark")
                backdrop = Color(0xFF4386E2); scene.settle()
                assertTrue(rgbDifference(before, scene.bitmap().getRGB(195, y)) > 8,
                    "The composited backdrop must follow the scrolling content: kind=$kind dark=$dark")
                scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 300f)
                scene.settle()
                backdrop = Color(0xFFE26C48); scene.settle()
                assertTrue(rgbDifference(scene.bitmap().getRGB(195, y), before) < 8,
                    "Backdrop must keep updating after scrolling: kind=$kind dark=$dark")
            }
        }
    }

    @Test
    fun fullHeightNestedScaffoldDrawsLiveScrollingPixelsThroughTheDockMargins() {
        var backdrop by mutableStateOf(Color(0xFFE26C48))
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme {
                BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                    selectedTab = BottomBarConfiguration.Home, onClickTab = {}) {
                    Box(Modifier.fillMaxSize().clip(RectangleShape)) {
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
            val y = (scene.tabs().first().boundsInRoot.top - 3f).toInt()
            val before = scene.bitmap().getRGB(195, y)
            assertTrue(rgbDifference(scene.bitmap().getRGB(6, y), backdrop.toArgb()) < 8,
                "The transparent side of the dock must show the scrolling content")
            backdrop = Color(0xFF4386E2); scene.settle()
            assertTrue(rgbDifference(before, scene.bitmap().getRGB(195, y)) > 8,
                "Nested Scaffold must not clip away the background sampled by root glass")
            scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
            scene.settle()
            assertTrue(scene.text("NESTED_LAST").boundsInRoot.bottom <= scene.tabs().first().boundsInRoot.top)
            scene.save("root-glass-nested-scaffold")
        }
    }

    @Test
    fun dockScrimStartsAtItsCenterAndFadesIntoTheScreenBackground() {
        val backdrop = Color(0xFFE26C48)
        for (dark in listOf(false, true)) {
            val background = com.markettwits.core_ui.items.theme.sauceColors(dark).background
            ImageComposeScene(width = 390, height = 844) {
                SauceTheme(darkTheme = dark) {
                    BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                        selectedTab = BottomBarConfiguration.Home, onClickTab = {}) {
                        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(backdrop)) {
                            Spacer(Modifier.height(1800.dp))
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val centerY = scene.tabs().first().boundsInRoot.center.y.toInt()
                val image = scene.bitmap()
                assertTrue(rgbDifference(image.getRGB(6, centerY - 4), backdrop.toArgb()) < 8)
                val originalContrast = rgbDifference(backdrop.toArgb(), background.toArgb())
                assertTrue(rgbDifference(image.getRGB(6, centerY + 15), background.toArgb()) < originalContrast * 0.58f)
                assertTrue(rgbDifference(image.getRGB(6, image.height - 2), background.toArgb()) < 15)
            }
        }
    }

    @Test
    fun translucentScrollingContentRendersOnceBesideTheDockDuringTabChanges() {
        var selected by mutableStateOf<BottomBarConfiguration>(BottomBarConfiguration.Home)
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme {
                BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                    selectedTab = selected, onClickTab = {}) {
                    val tint = if (selected == BottomBarConfiguration.Home) Color.Red else Color.Blue
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                        .background(tint.copy(alpha = 0.35f))) {
                        Spacer(Modifier.height(1800.dp))
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            for (tab in listOf(BottomBarConfiguration.Home, BottomBarConfiguration.Review, BottomBarConfiguration.Home)) {
                selected = tab
                val image = scene.bitmap()
                val y = (scene.tabs().first().boundsInRoot.top - 3f).toInt()
                assertTrue(rgbDifference(image.getRGB(6, y), image.getRGB(6, y - 35)) < 8,
                    "A translucent layer must have the same color above and beside the dock")
            }
        }
    }

    @Test
    fun tappingTheDockKeepsTheLightHapticAndSelectsTheDestination() {
        val vibrations = mutableListOf<HapticFeedbackType>()
        val destinations = mutableListOf<BottomBarConfiguration>()
        val haptic = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {
                vibrations += hapticFeedbackType
            }
        }
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme {
                CompositionLocalProvider(LocalHapticFeedback provides haptic) {
                    BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                        selectedTab = BottomBarConfiguration.Home, onClickTab = destinations::add) {
                        Box(Modifier.fillMaxSize())
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val tab = scene.tabs().last().boundsInRoot.center
            scene.sendPointerEvent(PointerEventType.Press, tab)
            scene.sendPointerEvent(PointerEventType.Release, tab)
            assertEquals(listOf(HapticFeedbackType.TextHandleMove), vibrations)
            assertEquals(listOf<BottomBarConfiguration>(BottomBarConfiguration.Profile), destinations)
        }
    }

    @Test
    fun overlappingScreensCannotReplayEachOthersScrollLayersAtTheDock() {
        val outgoing = Color(0xFFE26C48)
        val incoming = Color(0xFF4386E2)
        var overlap by mutableStateOf(true)
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme {
                BottomBarContent(isShowTopBar = true, isShowLabel = true, items = defaultMenuItems(),
                    selectedTab = BottomBarConfiguration.Review, onClickTab = {}) {
                    Box(Modifier.fillMaxSize().clip(RectangleShape)) {
                        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(incoming)) {
                            Spacer(Modifier.height(1800.dp))
                        }
                        if (overlap) {
                            Box(Modifier.fillMaxSize().graphicsLayer { alpha = 0f }) {
                                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(outgoing)) {
                                    Spacer(Modifier.height(1800.dp))
                                }
                            }
                        }
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val y = (scene.tabs().first().boundsInRoot.top - 3f).toInt()
            assertTrue(rgbDifference(scene.bitmap().getRGB(6, y), outgoing.toArgb()) > 50)
            overlap = false
            scene.settle()
            assertTrue(rgbDifference(scene.bitmap().getRGB(6, y), incoming.toArgb()) < 8)
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
                val y = (firstBounds.first().top - 3f).toInt()
                val a = first.getRGB(195, y); val b = second.getRGB(195, y)
                assertTrue(rgbDifference(a, b) > 8, "Glass must update from actual background pixels: dark=$dark scale=$scale")
                assertTrue(firstBounds.first().left in 16f..45f && firstBounds.last().right in 345f..374f)
                assertTrue(firstBounds.first().bottom <= 826f, "The capsule must float above the bottom edge")
                assertTrue(rgbDifference(first.getRGB(6, y), 0xFFE26C48.toInt()) < 8)
                assertTrue(rgbDifference(second.getRGB(6, y), 0xFF4386E2.toInt()) < 8)
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
                val y = (bounds.first().top - 3f).toInt()
                val values = (168..182).map { image.getRGB(it, y) and 255 }
                assertTrue(values.max() - values.min() < 8, "Fine stripes should disappear under strong blur: $values")
                assertTrue(rgbDifference(image.getRGB(4, y), image.getRGB(6, y)) > 700,
                    "Content outside the capsule must remain sharp")
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
                    } else Scaffold(
                        bottomBar = { Text("PINNED_ACTION", Modifier.height(56.dp)) },
                        snackbarHost = { Text("SNACK_MESSAGE", Modifier.height(42.dp)) },
                        floatingActionButton = { Text("FLOATING_ACTION", Modifier.height(48.dp)) },
                    ) { padding ->
                        Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                            Text("FORM_CONTENT", Modifier.align(Alignment.BottomCenter))
                        }
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val scroll = scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
            assertEquals(844f, scroll.boundsInRoot.bottom, "The viewport must extend under the bar")
            scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f); scene.settle()
            val last = scene.text("LAST_ITEM")
            assertTrue(last.boundsInRoot.height > 0f)
            assertTrue(last.boundsInRoot.bottom <= scene.tabs().first().boundsInRoot.top)
            underlay = false; scene.settle()
            val dockTop = scene.tabs().first().boundsInRoot.top
            val footer = scene.text("PINNED_ACTION").boundsInRoot
            val fab = scene.text("FLOATING_ACTION").boundsInRoot
            assertTrue(footer.bottom <= dockTop)
            assertTrue(scene.text("FORM_CONTENT").boundsInRoot.bottom <= footer.top)
            assertTrue(fab.bottom <= footer.top)
            assertTrue(scene.text("SNACK_MESSAGE").boundsInRoot.bottom <= fab.top)
            visible = false; scene.settle()
            assertTrue(scene.tabs().isEmpty())
            assertEquals(844f, scene.text("PINNED_ACTION").boundsInRoot.bottom)
            visible = true; scene.settle()
            assertEquals(4, scene.tabs().size)
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
                val noticeCenter = Offset(195f, tabsBefore.first().top - 38f)
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
            val layout = layouts.single()
            assertFalse(layout.hasVisualOverflow,
                "Clipped content: ${layout.layoutInput.text}, size=${layout.size}, width=${layout.didOverflowWidth}, height=${layout.didOverflowHeight}")
        }
    }
    private fun rgbDifference(a: Int, b: Int) = listOf(0, 8, 16).sumOf { abs(((a shr it) and 255) - ((b shr it) and 255)) }
}
