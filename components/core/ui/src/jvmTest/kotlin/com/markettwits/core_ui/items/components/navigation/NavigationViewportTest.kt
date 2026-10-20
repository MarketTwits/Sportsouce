package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class NavigationViewportTest {
    @Test
    fun everyVerticalPrimitiveExtendsNestedViewportsAndKeepsItsLastItemAboveNavigation() {
        for (kind in 0..3) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 390, height = 844, density = Density(1f, scale)) {
                Host {
                    // Match the nested boxes and layers used by navigation stacks.
                    Box(Modifier.fillMaxSize().graphicsLayer { alpha = 1f }) {
                        Box(Modifier.fillMaxSize()) {
                            when (kind) {
                                0 -> Column(Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState())) {
                                    repeat(30) { Text("Item $it", Modifier.height(60.dp)) }
                                }
                                1 -> SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp)) {
                                    items(30, key = { it }) { Text("Item $it", Modifier.height(60.dp)) }
                                }
                                2 -> SauceLazyVerticalGrid(GridCells.Fixed(2), Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp)) {
                                    items(60, key = { it }) { Text("Item $it", Modifier.height(60.dp)) }
                                }
                                3 -> SauceLazyVerticalStaggeredGrid(StaggeredGridCells.Fixed(2), Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp)) {
                                    items(60, key = { it }) { Text("Item $it", Modifier.height(60.dp)) }
                                }
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val scroll = scene.scrolls().single()
                assertEquals(844f, scroll.boundsInRoot.bottom, "kind=$kind font=$scale")
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f); scene.settle()
                val last = scene.text("Item ${if (kind < 2) 29 else 59}")
                assertTrue(last.boundsInRoot.height > 0f)
                assertTrue(last.boundsInRoot.bottom <= 740f, "Last item hidden: kind=$kind bounds=${last.boundsInRoot}")
                assertTrue(scene.scrolls().single().config[SemanticsProperties.VerticalScrollAxisRange].maxValue() > 0f)
            }
        }
    }

    @Test
    fun nestedScrollAreasDoNotAddAnotherOverlapAndPinnedActionsStayInTheirOriginalSlot() {
        ImageComposeScene(width = 390, height = 844) {
            Host {
                Box(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(640.dp))
                        Column(Modifier.fillMaxWidth().height(100.dp).sauceVerticalScroll(rememberScrollState())) {
                            Spacer(Modifier.height(300.dp))
                        }
                        Spacer(Modifier.height(1000.dp))
                    }
                    Text("PINNED", Modifier.align(Alignment.BottomCenter))
                }
            }
        }.use { scene ->
            scene.settle()
            assertEquals(740f, scene.text("PINNED").boundsInRoot.bottom)
            val inner = scene.scrolls().single { it.size.height == 100 }
            assertEquals(200f, inner.config[SemanticsProperties.VerticalScrollAxisRange].maxValue(),
                "Inner scroll must not get another navigation spacer")
            assertEquals(844f, scene.scrolls().maxOf { it.boundsInRoot.bottom })
        }
    }

    @Test
    fun scrollAboveAFixedFooterAndModalContentKeepTheirOriginalBounds() {
        for (modal in listOf(false, true)) {
            ImageComposeScene(width = 390, height = 844) {
                Host {
                    CompositionLocalProvider(LocalNavigationViewport provides if (modal) null else LocalNavigationViewport.current) {
                        Column(Modifier.fillMaxSize()) {
                            Column(Modifier.weight(1f).sauceVerticalScroll(rememberScrollState())) {
                                Spacer(Modifier.height(1800.dp))
                                Text("LAST")
                            }
                            Text("FOOTER", Modifier.height(56.dp))
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                assertEquals(684f, scene.scrolls().single().boundsInRoot.bottom)
                assertEquals(740f, scene.text("FOOTER").boundsInRoot.bottom)
                scene.scrolls().single().config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f); scene.settle()
                assertTrue(scene.text("LAST").boundsInRoot.bottom <= 684f)
            }
        }
    }

    @Test
    fun disablingOrHidingRootChromeRemovesOverlapWithoutResettingScroll() {
        var overlap by mutableIntStateOf(104)
        ImageComposeScene(width = 390, height = 844) {
            Host(overlap) {
                SauceLazyColumn(Modifier.fillMaxSize()) {
                    items(60, key = { it }) { Text("Item $it", Modifier.height(60.dp)) }
                }
            }
        }.use { scene ->
            scene.settle()
            scene.scrolls().single().config[SemanticsActions.ScrollBy].action!!.invoke(0f, 600f); scene.settle()
            val before = scene.scrolls().single().config[SemanticsProperties.VerticalScrollAxisRange].value()
            overlap = 0; scene.settle()
            assertEquals(844f, scene.scrolls().single().boundsInRoot.bottom)
            assertEquals(before, scene.scrolls().single().config[SemanticsProperties.VerticalScrollAxisRange].value())
            overlap = 104; scene.settle()
            assertEquals(before, scene.scrolls().single().config[SemanticsProperties.VerticalScrollAxisRange].value())
        }
    }

    @Composable
    private fun Host(overlap: Int = 104, content: @Composable () -> Unit) {
        val viewport = remember { NavigationViewport() }
        Box(Modifier.fillMaxSize().onGloballyPositioned { viewport.update(it, overlap) }) {
            CompositionLocalProvider(LocalNavigationViewport provides viewport) {
                Box(Modifier.fillMaxSize().padding(bottom = overlap.dp)) { content() }
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(15) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.scrolls() = nodes().filter { it.config.contains(SemanticsActions.ScrollBy) }
    private fun ImageComposeScene.text(label: String) = nodes().single {
        it.config.getOrNull(SemanticsProperties.Text)?.any { text -> text.text == label } == true
    }
}
