package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.graphics.Color
import java.io.ByteArrayInputStream
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.components.toolbar.*
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.*
import java.io.File
import javax.imageio.ImageIO

@OptIn(ExperimentalComposeUiApi::class)
class TopBarGlassTest {
    @Test
    fun allScrollPrimitivesRevealRealContentThroughGlassAcrossAncestorClips() {
        for (dark in listOf(false, true)) for (kind in 0..3) {
            ImageComposeScene(width = 390, height = 700) {
                SauceTheme(darkTheme = dark) {
                    SauceScaffold(topBar = { TopBarBase(title = "Панель", isStatusBarHandle = false, goBack = {}) }) { padding ->
                        Box(Modifier.fillMaxSize().padding(padding).clipToBounds()) {
                            when (kind) {
                                0 -> Column(Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState())) {
                                    repeat(40) { RowItem(it) }
                                }
                                1 -> SauceLazyColumn(Modifier.fillMaxSize()) { items(40) { RowItem(it) } }
                                2 -> SauceLazyVerticalGrid(GridCells.Fixed(2), Modifier.fillMaxSize()) { items(40) { RowItem(it) } }
                                3 -> SauceLazyVerticalStaggeredGrid(StaggeredGridCells.Fixed(2), Modifier.fillMaxSize()) { items(40) { RowItem(it) } }
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val toolbar = scene.text("Панель").boundsInRoot
                val first = scene.text("Item 0").boundsInRoot
                assertTrue(first.top >= 56f, "Initial content must clear the toolbar: $first")
                val before = scene.renderImage().getRGB(370, 28)
                val scroll = scene.scrolls().single()
                // Ancestor clipping still bounds touch/semantics; glass samples the retained underlay.
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 220f)
                scene.settle()
                val after = scene.renderImage().getRGB(370, 28)
                assertTrue(rgbDifference(before, after) > 15, "Glass must sample scrolling content even with ancestor clipping: kind=$kind dark=$dark before=$before after=$after")
                assertEquals(toolbar, scene.text("Панель").boundsInRoot)
                val destination = File("build/reports/topbar-glass/${if (dark) "dark" else "light"}-$kind.png")
                destination.parentFile.mkdirs(); ImageIO.write(scene.renderImage(), "png", destination)
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
                assertTrue(scene.text("Item 39").boundsInRoot.bottom <= 700f)
            }
        }
    }

    @Test
    fun collapsingToolbarUsesSameBackdropAndPreservesItsScrollState() {
        for (strategy in listOf(ScrollStrategy.EnterAlways, ScrollStrategy.EnterAlwaysCollapsed, ScrollStrategy.ExitUntilCollapsed)) {
            var color by mutableStateOf(Color(0xFFFF5030))
            ImageComposeScene(width = 390, height = 700) {
                SauceTheme {
                    CollapsingToolbarScaffold(Modifier.fillMaxSize(), rememberCollapsingToolbarScaffoldState(), strategy,
                        toolbar = { Text("Collapsed", Modifier.fillMaxWidth().height(64.dp)) }) {
                        Column(Modifier.fillMaxSize().clipToBounds().sauceVerticalScroll(rememberScrollState())) {
                            repeat(30) { Text("Item $it", Modifier.fillMaxWidth().height(60.dp).background(color)) }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val scroll = scene.scrolls().single()
                assertTrue(scroll.boundsInRoot.top >= 0f)
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 200f); scene.settle()
                val a = scene.renderImage().getRGB(370, 28)
                val position = scroll.config[SemanticsProperties.VerticalScrollAxisRange].value()
                color = Color(0xFF3080FF); scene.settle()
                val b = scene.renderImage().getRGB(370, 28)
                assertTrue(rgbDifference(a, b) > 15, "Collapsing toolbar must sample its body: $strategy")
                assertEquals(position, scene.scrolls().single().config[SemanticsProperties.VerticalScrollAxisRange].value())
            }
        }
    }

    @Test
    fun pinnedFiltersAndFootersKeepTheirPixelsWhileContentFeedsGlass() {
        ImageComposeScene(width = 390, height = 700) {
            SauceTheme {
                SauceScaffold(topBar = { TopBarBase(title = "Панель", isStatusBarHandle = false, goBack = {}) }) { padding ->
                    Column(Modifier.fillMaxSize().padding(padding)) {
                        Text("PINNED", Modifier.fillMaxWidth().height(48.dp).background(Color.Blue))
                        SauceLazyColumn(Modifier.weight(1f)) { items(40) { RowItem(it) } }
                        Text("FOOTER", Modifier.fillMaxWidth().height(48.dp).background(Color.Blue))
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            val pinned = scene.text("PINNED").boundsInRoot
            val footer = scene.text("FOOTER").boundsInRoot
            val before = scene.renderImage().getRGB(370, 28)
            scene.scrolls().single().config[SemanticsActions.ScrollBy].action!!.invoke(0f, 300f)
            scene.settle()
            val image = scene.renderImage()
            assertEquals(Color.Blue.toArgb(), image.getRGB(370, 80), "Expanded content must not draw over filters")
            assertEquals(Color.Blue.toArgb(), image.getRGB(370, 680), "Footer must retain its own surface")
            assertTrue(rgbDifference(before, image.getRGB(370, 28)) > 15)
            assertEquals(pinned, scene.text("PINNED").boundsInRoot)
            assertEquals(footer, scene.text("FOOTER").boundsInRoot)
        }
    }

    @Test
    fun fineDetailsBlurWhileFallbackKeepsToolbarGeometryAndInteractions() {
        for (dark in listOf(false, true)) {
            var blur by mutableStateOf(true)
            var backCount = 0
            ImageComposeScene(width = 390, height = 220) {
                SauceTheme(darkTheme = dark) {
                    SauceScaffold(topBar = { TopBarBase(title = "Панель", isStatusBarHandle = false, goBack = { backCount++ }) }) {
                        LocalSauceTopBar.current!!.haze.blurEnabled = blur
                        SauceTopBarBackdrop(Modifier.fillMaxSize()) {
                            Canvas(Modifier.fillMaxSize()) {
                                repeat(195) { i -> drawRect(if (i % 2 == 0) Color.Black else Color.White,
                                    topLeft = Offset(i * 2f, 0f), size = Size(2f, size.height)) }
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val toolbar = scene.text("Панель").boundsInRoot
                val image = scene.renderImage()
                val values = (340..355).map { image.getRGB(it, 28) and 255 }
                assertTrue(values.max() - values.min() < 8, "Fine detail must be blurred: $values")
                assertTrue(rgbDifference(image.getRGB(340, 160), image.getRGB(342, 160)) > 700,
                    "Body must stay sharp")
                blur = false; scene.settle()
                assertEquals(toolbar, scene.text("Панель").boundsInRoot)
                val back = scene.nodes().single { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Назад") == true }
                scene.sendPointerEvent(PointerEventType.Press, back.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, back.boundsInRoot.center)
                assertEquals(1, backCount)
            }
        }
    }

    @Composable private fun RowItem(index: Int) {
        Text("Item $index", Modifier.fillMaxWidth().height(60.dp).background(Color(0xFFFF5030)))
    }
    private fun rgbDifference(a: Int, b: Int) = listOf(0, 8, 16).sumOf { kotlin.math.abs(((a shr it) and 255) - ((b shr it) and 255)) }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(20) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.renderImage() = render(time.also { time += 100_000_000L }).use { image ->
        image.encodeToData()!!.use { ImageIO.read(ByteArrayInputStream(it.bytes)) }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.scrolls() = nodes().filter { it.config.contains(SemanticsActions.ScrollBy) }
    private fun ImageComposeScene.text(text: String) = nodes().single { it.config.getOrNull(SemanticsProperties.Text)?.any { s -> s.text == text } == true }
}
