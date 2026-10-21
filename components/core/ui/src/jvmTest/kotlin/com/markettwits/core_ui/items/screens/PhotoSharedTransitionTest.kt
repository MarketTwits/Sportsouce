package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.*
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class PhotoSharedTransitionTest {
    private val source = Rect(130f, 320f, 259f, 449f)
    private val viewport = Size(390f, 900f)
    private val painter = object : Painter() {
        override val intrinsicSize = Size(600f, 400f)
        override fun DrawScope.onDraw() {
            drawRect(Color(0xFF427956))
            drawRect(Color(0xFFF1B64E), Offset(size.width / 3, 0f), Size(size.width / 3, size.height))
        }
    }
    @Test fun transitionStartsAtTheCropAndRevealsTheOriginalAspectRatioInBothDirections() {
        for (image in listOf(Size(600f, 400f), Size(400f, 600f))) {
            val start = photoTransitionGeometry(source, image, viewport, 0f, 2f)
            assertEquals(source, start.clip)
            assertEquals(source.center, start.image.center)
            assertTrue(start.image.width >= source.width && start.image.height >= source.height)
            assertEquals(2f, start.cornerRadius)
            val end = photoTransitionGeometry(source, image, viewport, 1f, 2f)
            assertEquals(end.clip, end.image)
            assertEquals(Offset(195f, 450f), end.image.center)
            assertEquals(image.width / image.height, end.image.width / end.image.height, .001f)
            assertEquals(0f, end.cornerRadius)
            for (progress in listOf(.1f, .5f, .9f)) {
                val frame = photoTransitionGeometry(source, image, viewport, progress, 2f)
                assertTrue(frame.image.left <= frame.clip.left + .01f && frame.image.right >= frame.clip.right - .01f)
                assertTrue(frame.image.top <= frame.clip.top + .01f && frame.image.bottom >= frame.clip.bottom - .01f)
            }
        }
    }
    @Test fun albumRemainsVisibleAtTheStartAndViewerControlsAppearOnlyAfterThePhotoExpands() {
        var time = System.nanoTime()
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme(darkTheme = true) {
                Box(Modifier.fillMaxSize().background(Color.Magenta)) {
                    PhotoViewerContent(List(4) { "" }, 0, "Фото", 4, false, {}, {}, {},
                        transitionSource = PhotoTransitionSource(source, painter, 2f))
                }
            }
        }.use { scene ->
            fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
            fun hasClose() = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.any {
                "Закрыть фото" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty()
            }
            scene.render(time).use { frame ->
                val pixel = frame.toComposeImageBitmap().toPixelMap()[5, 280]
                assertEquals(Color.Magenta, pixel, "The first frame must retain the album, without a fullscreen viewer backdrop")
            }
            assertFalse(hasClose(), "The toolbar must not appear before the image transition")
            repeat(3) { time += 30_000_000; scene.render(time).close(); assertFalse(hasClose()) }
            repeat(16) { time += 40_000_000; scene.render(time).close() }
            assertTrue(hasClose())
        }
    }
    @Test fun closePreparesTheCurrentThumbnailBeforeAnimatingAndDismissing() {
        var prepared = -1
        var dismissed = -1
        val anchor = PhotoTransitionSource(source, painter, 2f)
        var time = System.nanoTime()
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme(darkTheme = true) {
                PhotoViewerContent(List(4) { "" }, 2, "Фото", 4, false, {}, {}, { dismissed = it },
                    transitionSource = anchor, prepareReturn = { index -> prepared = index; anchor })
            }
        }.use { scene ->
            repeat(16) { time += 40_000_000; scene.render(time).close() }
            fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
            var close = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.first {
                "Закрыть фото" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty()
            }
            while (!close.config.contains(SemanticsActions.OnClick)) close = checkNotNull(close.parent)
            close.config[SemanticsActions.OnClick].action!!.invoke()
            repeat(16) { time += 40_000_000; scene.render(time).close() }
            assertEquals(2, prepared)
            assertEquals(2, dismissed)
        }
    }
    @Test fun transitionFramesRenderTheSamePhotoAcrossTheDialogBoundary() {
        for (progress in listOf(0f, .5f, 1f)) {
            ImageComposeScene(width = 390, height = 900) {
                PhotoSharedTransition(PhotoTransitionSource(source, painter, 2f), progress, Offset.Zero, Modifier.fillMaxSize())
            }.use { scene ->
                scene.render(System.nanoTime()).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/photo-shared-${(progress * 100).toInt()}.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }
}
