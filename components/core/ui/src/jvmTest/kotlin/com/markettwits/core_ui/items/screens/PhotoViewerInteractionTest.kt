package com.markettwits.core_ui.items.screens

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class PhotoViewerInteractionTest {
    private var time = System.nanoTime()

    @Test fun filmstripSelectionSurvivesAppendAndControlsToggleWithoutLosingThePage() {
        var images by mutableStateOf(List(6) { "" })
        var changed = -1
        var closed = -1
        var downloaded = -1
        var shared = -1
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme(darkTheme = true) {
                PhotoViewerContent(images, 1, "Фотоальбом", 60, false, {}, { changed = it }, { closed = it },
                    onDownload = { downloaded = it }, onShare = { shared = it })
            }
        }.use { scene ->
            scene.settle()
            assertEquals(1, changed)
            scene.clickLabel("Миниатюра 4"); scene.settle()
            assertEquals(3, changed); assertTrue(scene.texts().contains("4 / 60"))
            scene.clickLabel("Скачать оригинал"); scene.clickLabel("Поделиться оригиналом")
            assertEquals(3, downloaded); assertEquals(3, shared)
            images = List(36) { "" }; scene.settle()
            assertTrue(scene.texts().contains("4 / 60")); assertEquals(3, changed)
            scene.clickLabel("Просмотр фотографии"); scene.settle()
            assertFalse(scene.nodes().any { "Закрыть фото" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
            scene.clickLabel("Просмотр фотографии"); scene.settle()
            assertTrue(scene.texts().contains("4 / 60"))
            scene.clickLabel("Закрыть фото"); scene.settle()
            assertEquals(3, closed)
        }
    }

    @Test fun zoomedPhotoDoesNotDismissOnVerticalPanAndCanBeReset() {
        var closed = -1
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme(darkTheme = true) {
                PhotoViewerContent(List(4) { "" }, 0, "Фотоальбом", 4, false, {}, {}, { closed = it })
            }
        }.use { scene ->
            scene.settle()
            val image = scene.label("Просмотр фотографии")
            image.config[SemanticsActions.CustomActions].single().action.invoke()
            scene.settle()
            assertTrue(scene.nodes().any { "Сбросить масштаб" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
            scene.swipe(Offset(195f, 400f), Offset(195f, 710f)); scene.settle()
            assertEquals(-1, closed)
            scene.clickLabel("Сбросить масштаб"); scene.settle()
            assertFalse(scene.nodes().any { "Сбросить масштаб" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
            scene.swipe(Offset(195f, 400f), Offset(195f, 710f)); scene.settle()
            assertEquals(0, closed)
        }
    }

    @Test fun viewerControlsAndFilmstripFitLargeTextAndLandscape() {
        for ((width, height, scale) in listOf(Triple(320, 900, 2f), Triple(800, 450, 1f))) {
            ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                SauceTheme(darkTheme = true) {
                    PhotoViewerContent(List(8) { "" }, 0, "Фотографии с осеннего старта", 791, false, {}, {}, {}, onShare = {}, onDownload = {})
                }
            }.use { scene ->
                scene.settle()
                scene.nodes().forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
                        assertFalse(it.didOverflowHeight, "Clipped text: ${it.layoutInput.text}")
                    }
                }
                assertTrue(scene.texts().contains("1 / 791"))
                scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/photo-viewer-$width.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
    private fun ImageComposeScene.settle() { repeat(24) { time += 50_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.label(s: String) = nodes().first { s in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { t -> t.text } }
    private fun ImageComposeScene.clickLabel(s: String) {
        var node = label(s)
        while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
        node.config[SemanticsActions.OnClick].action!!.invoke()
    }
    private fun ImageComposeScene.swipe(start: Offset, end: Offset) {
        sendPointerEvent(PointerEventType.Press, start)
        repeat(12) { step ->
            time += 20_000_000L
            sendPointerEvent(PointerEventType.Move, start + (end - start) * ((step + 1) / 12f), timeMillis = time / 1_000_000)
            render(time).close()
        }
        sendPointerEvent(PointerEventType.Release, end, timeMillis = time / 1_000_000)
    }
}
