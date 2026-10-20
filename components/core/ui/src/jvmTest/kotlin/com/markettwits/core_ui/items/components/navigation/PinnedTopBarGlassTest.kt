package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.components.toolbar.*
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class PinnedTopBarGlassTest {
    @Test
    fun toolbarAndPinnedRowsUseOneMaterialWithoutBlurringOrCapturingTheirControls() {
        for (dark in listOf(false, true)) {
            var color by mutableStateOf(Color(0xFFFF5030))
            var marker by mutableStateOf(Color.Green)
            var taps = 0
            ImageComposeScene(width = 390, height = 700) {
                SauceTheme(darkTheme = dark) {
                    SauceScaffold(topBar = { Toolbar() }) { padding ->
                        Column(Modifier.fillMaxSize().padding(padding)) {
                            SauceTopBarSection {
                                PinnedRow(marker) { taps++ }
                                // Adjacent rows are one material; a nested section must not add a second tint.
                                SauceTopBarSection { Text("FILTERS", Modifier.height(48.dp)) }
                            }
                            ScrollContent(color, Modifier.weight(1f))
                            Text("FOOTER", Modifier.fillMaxWidth().height(48.dp).background(Color.Blue))
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val tabs = scene.text("TABS").boundsInRoot
                val footer = scene.text("FOOTER").boundsInRoot
                assertTrue(scene.text("Item 0").boundsInRoot.top >= 160f)
                val before = scene.bitmap()
                scene.scroll(300f); scene.settle()
                val after = scene.bitmap()
                for (y in listOf(28, 80, 136)) assertTrue(rgbDifference(before.getRGB(370, y), after.getRGB(370, y)) > 15,
                    "All chrome rows must sample live content: dark=$dark y=$y")
                assertTrue(rgbDifference(after.getRGB(370, 55), after.getRGB(370, 57)) < 8, "No material seam at the toolbar edge")
                assertTrue(rgbDifference(after.getRGB(370, 80), after.getRGB(370, 136)) < 8, "Nested rows must not double-tint")
                assertEquals(Color.Green.toArgb(), after.getRGB(195, 84), "Pinned controls must stay sharp")
                marker = Color.Yellow; scene.settle()
                assertTrue(rgbDifference(after.getRGB(370, 28), scene.bitmap().getRGB(370, 28)) < 8,
                    "Pinned controls must never feed the backdrop")
                assertEquals(tabs, scene.text("TABS").boundsInRoot)
                assertEquals(footer, scene.text("FOOTER").boundsInRoot)
                scene.tap("TABS"); assertEquals(1, taps)
                color = Color(0xFF3080FF); scene.settle()
                assertTrue(rgbDifference(after.getRGB(370, 80), scene.bitmap().getRGB(370, 80)) > 15)
                scene.save("pinned-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test
    fun sectionsKeepGlassWhenTheCollapsingToolbarIsHidden() {
        for (dark in listOf(false, true)) {
            lateinit var collapsing: CollapsingToolbarScaffoldState
            ImageComposeScene(width = 390, height = 700) {
                SauceTheme(darkTheme = dark) {
                    collapsing = rememberCollapsingToolbarScaffoldState()
                    CollapsingToolbarScaffold(Modifier.fillMaxSize(), collapsing, ScrollStrategy.EnterAlwaysCollapsed,
                        toolbar = { Toolbar() }) {
                        Column(Modifier.fillMaxSize()) {
                            SauceTopBarSection { PinnedRow(Color.Green) {} }
                            ScrollContent(Color(0xFFFF5030), Modifier.weight(1f))
                        }
                    }
                }
            }.use { scene ->
                scene.settle(); scene.scroll(300f); scene.settle()
                // Semantics scrolling also drives nested scroll and hides the toolbar.
                collapsing.offsetYState.value = 0; scene.settle()
                val before = scene.bitmap().getRGB(370, 84)
                val scrollPosition = scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value()
                collapsing.offsetYState.value = -56; scene.settle()
                assertEquals(0f, scene.text("TABS").boundsInRoot.top)
                assertTrue(rgbDifference(before, scene.bitmap().getRGB(370, 28)) < 8,
                    "Pinned material must remain identical after toolbar collapse: dark=$dark")
                assertEquals(scrollPosition, scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value())
                scene.save("pinned-collapsed-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test
    fun collapsedSectionsProtectSystemIconsWithoutAddingTheInsetTwice() {
        lateinit var collapsing: CollapsingToolbarScaffoldState
        ImageComposeScene(width = 390, height = 700) {
            SauceTheme {
                collapsing = rememberCollapsingToolbarScaffoldState()
                CollapsingToolbarScaffold(Modifier.fillMaxSize(), collapsing, ScrollStrategy.EnterAlwaysCollapsed,
                    toolbar = { Toolbar() }) {
                    Column(Modifier.fillMaxSize()) {
                        SauceTopBarSection(windowInsets = WindowInsets(top = 24.dp)) {
                            PinnedRow(Color.Green) {}
                            SauceTopBarSection(windowInsets = WindowInsets(top = 24.dp)) {
                                Text("FILTERS", Modifier.height(48.dp))
                            }
                        }
                        ScrollContent(Color.Red, Modifier.weight(1f))
                    }
                }
            }
        }.use { scene ->
            scene.settle()
            assertEquals(56f, scene.text("TABS").boundsInRoot.top, "Expanded toolbar already protects the section")
            scene.scroll(300f); scene.settle()
            assertEquals(24f, scene.text("TABS").boundsInRoot.top, "Collapsed controls clear system icons")
            assertEquals(80f, scene.text("FILTERS").boundsInRoot.top, "Nested rows reuse the same inset")
            assertTrue(rgbDifference(scene.bitmap().getRGB(370, 12), scene.bitmap().getRGB(370, 44)) < 8,
                "The safe area must share the section's glass")
            val offset = scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value()
            collapsing.offsetYState.value = 0; scene.settle()
            assertEquals(56f, scene.text("TABS").boundsInRoot.top)
            assertEquals(offset, scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value())
        }
    }

    @Test
    fun disappearingSectionsReleaseTheirGeometryAndCanReturnWithoutResettingScroll() {
        var visible by mutableStateOf(true)
        lateinit var host: SauceTopBarState
        ImageComposeScene(width = 390, height = 700) {
            SauceTheme {
                SauceScaffold(topBar = { Toolbar() }) { padding ->
                    host = LocalSauceTopBar.current!!
                    Column(Modifier.fillMaxSize().padding(padding)) {
                        SauceTopBarSection { PinnedRow(Color.Green) {} }
                        if (visible) SauceTopBarSection { Text("FILTERS", Modifier.height(48.dp)) }
                        ScrollContent(Color.Red, Modifier.weight(1f))
                    }
                }
            }
        }.use { scene ->
            scene.settle(); scene.scroll(300f); scene.settle()
            assertEquals(160f, host.bounds!!.rect.bottom)
            val offset = scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value()
            visible = false; scene.settle()
            assertEquals(112f, host.bounds!!.rect.bottom, "Disposed filter geometry must be removed")
            assertEquals(offset, scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value())
            visible = true; scene.settle()
            assertEquals(160f, host.bounds!!.rect.bottom)
            assertEquals(offset, scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value())
        }
    }

    @Test
    fun pinnedRowsAlsoWorkWithAnEmptyPrimaryToolbar() {
        ImageComposeScene(width = 390, height = 700) {
            SauceTheme {
                SauceScaffold { padding ->
                    Column(Modifier.fillMaxSize().padding(padding)) {
                        SauceTopBarSection { PinnedRow(Color.Green) {} }
                        ScrollContent(Color.Red, Modifier.weight(1f))
                    }
                }
            }
        }.use { scene ->
            scene.settle(); val before = scene.bitmap().getRGB(370, 28)
            scene.scroll(300f); scene.settle()
            assertTrue(rgbDifference(before, scene.bitmap().getRGB(370, 28)) > 15)
            assertEquals(0f, scene.text("TABS").boundsInRoot.top)
        }
    }

    @Composable private fun Toolbar() { Text("Toolbar", Modifier.fillMaxWidth().height(56.dp)) }
    @Composable private fun PinnedRow(marker: Color, onClick: () -> Unit) {
        Box(Modifier.fillMaxWidth().height(56.dp)) {
            Text("TABS", Modifier.height(56.dp).clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 16.dp))
            Box(Modifier.align(Alignment.Center).size(16.dp).background(marker))
        }
    }
    @Composable private fun ScrollContent(color: Color, modifier: Modifier) {
        Box(modifier.fillMaxWidth().clipToBounds()) {
            SauceLazyColumn(Modifier.fillMaxSize()) {
                items(40) { Text("Item $it", Modifier.fillMaxWidth().height(80.dp).background(color)) }
            }
        }
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(20) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.bitmap() = render(time.also { time += 100_000_000L }).use { image ->
        image.encodeToData()!!.use { ImageIO.read(ByteArrayInputStream(it.bytes)) }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().single {
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true
    }
    private fun ImageComposeScene.scrollNode() = nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
    private fun ImageComposeScene.scroll(y: Float) { scrollNode().config[SemanticsActions.ScrollBy].action!!.invoke(0f, y) }
    private fun ImageComposeScene.tap(label: String) {
        val center = text(label).boundsInRoot.center
        sendPointerEvent(PointerEventType.Press, center); sendPointerEvent(PointerEventType.Release, center)
    }
    private fun rgbDifference(a: Int, b: Int) = listOf(0, 8, 16).sumOf { kotlin.math.abs(((a shr it) and 255) - ((b shr it) and 255)) }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/reports/topbar-glass/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
