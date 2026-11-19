package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.bottom_sheet.SauceModalBottomSheet
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
class SheetScrollStabilityTest {
    @Test
    fun repeatedFlingsAtTheEndDoNotMoveTheSheetOrObscureItsLastRow() {
        for (lazy in listOf(true, false)) for (dark in listOf(false, true)) for (edgeToEdge in listOf(false, true)) {
            lateinit var list: LazyListState
            lateinit var scroll: ScrollState
            var dismissals = 0
            ImageComposeScene(width = 360, height = 640) {
                SauceTheme(darkTheme = dark) {
                    SauceModalBottomSheet(
                        onDismissRequest = { dismissals++ },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                        windowInsets = WindowInsets(top = 32.dp, bottom = 24.dp),
                        edgeToEdgeContent = edgeToEdge,
                    ) {
                        Text("HEADER", Modifier.fillMaxWidth().height(48.dp))
                        if (lazy) {
                            list = rememberLazyListState(initialFirstVisibleItemIndex = 79)
                            SauceLazyColumn(Modifier.fillMaxWidth().height(520.dp).semantics { contentDescription = "SCROLL_BODY" }, state = list) {
                                items(80) { Text("Row $it", Modifier.fillMaxWidth().height(56.dp)) }
                            }
                        } else {
                            scroll = rememberScrollState(initial = Int.MAX_VALUE)
                            Column(Modifier.fillMaxWidth().height(520.dp).semantics { contentDescription = "SCROLL_BODY" }.sauceVerticalScroll(scroll)) {
                                repeat(80) { Text("Row $it", Modifier.fillMaxWidth().height(56.dp)) }
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.frames(60)
                assertFalse(if (lazy) list.canScrollForward else scroll.canScrollForward)
                val headerTop = scene.text("HEADER").boundsInRoot.top
                assertTrue(headerTop >= 32f)
                assertEquals(if (edgeToEdge) 640f else 616f, scene.node {
                    "SCROLL_BODY" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty()
                }.boundsInRoot.bottom, .5f, "The edge-to-edge viewport must extend behind the system navigation bar")
                repeat(4) {
                    scene.touch(PointerEventType.Press, Offset(180f, 540f))
                    repeat(8) { step ->
                        scene.touch(PointerEventType.Move, Offset(180f, 540f - (step + 1) * 36f))
                    }
                    scene.touch(PointerEventType.Release, Offset(180f, 252f))
                    scene.frames(90)
                    repeat(30) {
                        scene.frames(1)
                        assertEquals(headerTop, scene.text("HEADER").boundsInRoot.top, .5f,
                            "Sheet keeps moving after a fling (lazy=$lazy, dark=$dark)")
                    }
                    assertTrue(scene.text("Row 79").boundsInRoot.bottom <= 616.5f,
                        "Last row must remain above the bottom safe area")
                    assertEquals(0, dismissals)
                }
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.frames(count: Int) {
        repeat(count) { time += 16_666_667L; render(time).close() }
    }
    private fun ImageComposeScene.touch(type: PointerEventType, position: Offset) {
        sendPointerEvent(type, position, timeMillis = time / 1_000_000L, type = PointerType.Touch)
        frames(1)
    }
    private fun ImageComposeScene.text(value: String): SemanticsNode = node {
        it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == value }
    }
    private fun ImageComposeScene.node(predicate: (SemanticsNode) -> Boolean): SemanticsNode {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.first(predicate)
    }
}
