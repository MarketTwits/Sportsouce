package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import java.io.File
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class SheetInteractionTest {
    @Test
    fun tappingTheSheetHeadingDoesNotDismissItAndCloseRemainsASeparateTarget() {
        for (dark in listOf(false, true)) {
            var dismissed = 0
            ImageComposeScene(width = 360, height = 640, density = Density(1f, 2f)) {
                SauceTheme(darkTheme = dark) {
                    DefaultModalBottomSheet(title = "Параметры отображения", onDismissRequest = { dismissed++ }) {
                        Text("Содержимое панели", Modifier.padding(20.dp))
                    }
                }
            }.use { scene ->
                settle(scene)
                val heading = scene.nodes().first { it.config.contains(SemanticsProperties.Heading) }
                assertTrue(!heading.config.contains(SemanticsActions.OnClick), "The heading inherits the drag handle's dismiss click")
                scene.tap(heading)
                settle(scene)
                assertEquals(0, dismissed)
                val close = scene.nodes().first { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Закрыть") == true }
                assertTrue(close.boundsInRoot.width >= 48f && close.boundsInRoot.height >= 48f)
                scene.saveFrame("sheet-${if (dark) "dark" else "light"}-2x")
                scene.tap(close)
                assertEquals(1, dismissed)
            }
        }
    }

    @Test
    fun selectionDialogKeepsItsDoneActionVisibleWithLargeTextAndLongLabels() {
        val selected = mutableStateOf(emptyList<String>())
        var selections = 0
        val options = (1..12).map { "Вариант $it с длинным названием" }
        ImageComposeScene(width = 360, height = 640, density = Density(1f, 2f)) {
            SauceTheme(darkTheme = true) {
                ItemsTextFiledDialog(label = "Выберите подходящие варианты", values = selected.value,
                    items = options, onValueChanged = { item ->
                        selections++
                        selected.value = if (item in selected.value) selected.value - item else selected.value + item
                    })
            }
        }.use { scene ->
            settle(scene)
            scene.saveFrame("selection-anchor-dark-2x")
            scene.tap(scene.nodes().first { it.config.contains(SemanticsActions.OnClick) })
            settle(scene)
            val done = scene.nodes().first { "Готово" in it.texts() && it.config.contains(SemanticsActions.OnClick) }
            assertTrue(done.boundsInRoot.height >= 48f && done.boundsInRoot.bottom <= 640f)
            val row = scene.nodes().first { it.config.contains(SemanticsProperties.ToggleableState) }
            scene.tap(row)
            settle(scene)
            assertEquals(1, selections)
            assertEquals(1, selected.value.size)
            scene.saveFrame("selection-dialog-dark-2x")
            scene.tap(done)
            settle(scene)
            assertTrue(scene.nodes().none { "Готово" in it.texts() })
        }
    }

    private fun SemanticsNode.texts() = config.getOrNull(SemanticsProperties.Text)?.map { it.text }.orEmpty()
    private fun ImageComposeScene.tap(node: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }
    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(15) { scene.render(time + it * 100_000_000L).close() }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.saveFrame(name: String) {
        File("build/design-system/$name.png").apply {
            parentFile.mkdirs()
            render(System.nanoTime() + 2_000_000_000L).use { image ->
                checkNotNull(image.encodeToData()).use { writeBytes(it.bytes) }
            }
        }
    }
}
