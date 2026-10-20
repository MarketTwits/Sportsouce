package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.unit.Density
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.textField.ItemsDialog
import com.markettwits.core_ui.items.screens.AlertDialogScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class CompactDialogTest {
    @Test
    fun selectionRemainsReachableInShortWindowWithLargeText() {
        for (dark in listOf(false, true)) for ((width, height) in listOf(320 to 480, 640 to 320)) {
            var selected = 0
            var dismissed = 0
            ImageComposeScene(width = width, height = height, density = Density(1f, 2f)) {
                TestTheme(dark) {
                    ItemsDialog(values = (1..20).map { "Вариант $it" },
                        label = "Выберите подходящие варианты участия в мероприятии", selected = emptyList(),
                        onDismissRequest = { dismissed++ }, onValueChange = { selected++ })
                }
            }.use { scene ->
                settle(scene)
                val action = scene.button("Готово")
                assertTrue(action.boundsInRoot.height >= 48f && action.boundsInRoot.bottom <= height.toFloat(),
                    "Done action is clipped: ${action.boundsInRoot}")
                val list = scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                assertTrue(list.boundsInRoot.height >= 48f, "No viewport remains for selection: ${list.boundsInRoot}")
                scene.save("selection-${if (dark) "dark" else "light"}-$width-2x")
                list.config[SemanticsActions.ScrollBy].action?.invoke(0f, 10000f)
                settle(scene)
                val last = scene.button("Вариант 9")
                assertTrue(last.boundsInRoot.height >= 48f, "Cannot reach the last option")
                scene.tap(last)
                assertEquals(1, selected)
                scene.save("selection-${if (dark) "dark" else "light"}-$width-2x-scrolled")
                scene.tap(action)
                assertEquals(1, dismissed)
            }
        }
    }

    @Test
    fun confirmationKeepsActionsReachableWithLongTitleAndLargeText() {
        for (dark in listOf(false, true)) for ((width, height) in listOf(320 to 480, 640 to 320)) {
            var dismissed = 0
            ImageComposeScene(width = width, height = height, density = Density(1f, 2f)) {
                TestTheme(dark) {
                    AlertDialogScreen(onDismissRequest = { dismissed++ }, onClickOk = {},
                        title = "Вы действительно хотите удалить выбранные данные участника из списка?",
                        text = "Данные удалятся. ".repeat(50))
                }
            }.use { scene ->
                settle(scene)
                for (label in listOf("Да", "Нет")) {
                    val action = scene.button(label)
                    assertTrue(action.boundsInRoot.height >= 48f && action.boundsInRoot.bottom <= height.toFloat(),
                        "Action $label is clipped: ${action.boundsInRoot}")
                }
                val body = scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                assertTrue(body.boundsInRoot.height >= 48f, "Confirmation text cannot be read: ${body.boundsInRoot}")
                scene.save("confirmation-${if (dark) "dark" else "light"}-$width-2x")
                assertTrue(body.config[SemanticsActions.ScrollBy].action?.invoke(0f, 10000f) == true)
                settle(scene)
                scene.tap(scene.button("Нет"))
                assertEquals(1, dismissed)
            }
        }
    }

    @Test
    fun singleSelectionCanBeCancelledAfterScrollingWithoutChangingTheValue() {
        var selected = 0
        var dismissed = 0
        ImageComposeScene(width = 640, height = 320, density = Density(1f, 2f)) {
            TestTheme(false) {
                ItemsDialog(values = (1..20).map { "Вариант $it" },
                    label = "Выберите вариант участия", selected = "Вариант 1",
                    onDismissRequest = { dismissed++ }, onValueChange = { selected++ })
            }
        }.use { scene ->
            settle(scene)
            scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                .config[SemanticsActions.ScrollBy].action?.invoke(0f, 10000f)
            settle(scene)
            scene.tap(scene.button("Отмена"))
            assertEquals(1, dismissed)
            assertEquals(0, selected)
        }
    }

    @Composable
    private fun TestTheme(dark: Boolean, content: @Composable () -> Unit) {
        SauceTheme(darkTheme = dark) {
            Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) { content() }
        }
    }
    private fun ImageComposeScene.button(label: String) = nodes().first {
        label in it.texts() && it.config.contains(SemanticsActions.OnClick)
    }
    private fun SemanticsNode.texts() = config.getOrNull(SemanticsProperties.Text)?.map { it.text }.orEmpty()
    private fun ImageComposeScene.tap(node: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }
    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(12) { scene.render(time + it * 100_000_000L).close() }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.save(name: String) {
        File("build/design-system/$name.png").apply {
            parentFile.mkdirs()
            render(System.nanoTime() + 2_000_000_000L).use { image ->
                checkNotNull(image.encodeToData()).use { writeBytes(it.bytes) }
            }
        }
    }
}
