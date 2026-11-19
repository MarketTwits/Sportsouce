package com.markettwits.core_ui.items.screens

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.math.roundToInt
import kotlin.test.*
import javax.swing.SwingUtilities
import java.lang.reflect.InvocationTargetException

@OptIn(ExperimentalComposeUiApi::class)
class PhotoFilmstripInteractionTest {
    private var time = System.nanoTime()

    @Test fun stripTracksFractionalPagerMotionAndAppendWithoutRecenteringOrLosingThePage() = onUiThread {
        for (direction in listOf(LayoutDirection.Ltr, LayoutDirection.Rtl)) {
            var position by mutableFloatStateOf(10f)
            var count by mutableIntStateOf(30)
            ImageComposeScene(width = 390, height = 200) {
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    SauceTheme(darkTheme = true) {
                        PhotoFilmstrip(List(count) { "" }, position.roundToInt(), position = { position }, onSelect = { position = it.toFloat() })
                    }
                }
            }.use { scene ->
                scene.frames(8)
                assertEquals(195f, scene.label("Миниатюра 11").boundsInRoot.center.x, 1f)
                position = 10.25f; scene.frames(4)
                val expected = if (direction == LayoutDirection.Ltr) 180.5f else 209.5f
                assertEquals(expected, scene.label("Миниатюра 11").boundsInRoot.center.x, 1f, "The strip must move before the pager changes its integer page")
                position = 10.75f; scene.frames(4)
                val beforeAppend = scene.label("Миниатюра 12").boundsInRoot.center.x
                count = 60; scene.frames(4)
                assertEquals(beforeAppend, scene.label("Миниатюра 12").boundsInRoot.center.x, 1f)
                position = 0f; scene.frames(4)
                assertEquals(195f, scene.label("Миниатюра 1").boundsInRoot.center.x, 1f)
                position = 59f; scene.frames(4)
                assertEquals(195f, scene.label("Миниатюра 60").boundsInRoot.center.x, 1f)
            }
        }
    }

    @Test fun fastStripSwipeCoastsAfterReleaseThenCommitsCenteredPhotoWithLightHaptics() = onUiThread {
        val feedback = mutableListOf<HapticFeedbackType>()
        val displayed = mutableListOf<Int>()
        var downloaded = -1
        var closed = -1
        var imageCount by mutableIntStateOf(60)
        val haptic = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { feedback += hapticFeedbackType }
        }
        ImageComposeScene(width = 390, height = 900) {
            CompositionLocalProvider(LocalHapticFeedback provides haptic) {
                SauceTheme(darkTheme = true) {
                    PhotoViewerContent(List(imageCount) { "" }, 10, "Фотоальбом", imageCount, false, {}, { displayed += it }, { closed = it },
                        onDownload = { downloaded = it })
                }
            }
        }.use { scene ->
            scene.frames(30)
            assertTrue(feedback.isEmpty(), "Opening must not vibrate")
            val strip = scene.label("Лента миниатюр").boundsInRoot
            val start = Offset(strip.right - 45, strip.center.y)
            val end = Offset(strip.left + 45, strip.center.y)
            scene.pointer(PointerEventType.Press, start)
            repeat(6) { step -> scene.pointer(PointerEventType.Move, start + (end - start) * ((step + 1) / 6f)) }
            scene.pointer(PointerEventType.Release, end)
            val atRelease = displayed.last()
            assertTrue(scene.nodes().any { "Предпросмотр фотографии" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
            var downloadButton = scene.label("Скачать оригинал")
            while (!downloadButton.config.contains(SemanticsActions.OnClick)) downloadButton = checkNotNull(downloadButton.parent)
            assertTrue(downloadButton.config.contains(SemanticsProperties.Disabled), "Do not export the old page while scrubbing")
            scene.frames(15)
            assertTrue(displayed.last() > atRelease, "A fast swipe must keep moving after release")
            scene.frames(150)
            val final = displayed.last()
            assertTrue(final > atRelease)
            assertEquals(195f, scene.label("Миниатюра ${final + 1}").boundsInRoot.center.x, 1f)
            assertFalse(scene.nodes().any { "Предпросмотр фотографии" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
            assertTrue(feedback.isNotEmpty())
            assertTrue(feedback.all { it == HapticFeedbackType.TextHandleMove })
            assertEquals(displayed.size - 1, feedback.size, "Each displayed photo change vibrates once, without a second tick on commit")
            scene.click("Скачать оригинал")
            assertEquals(final, downloaded)
            val previousTicks = feedback.size
            scene.click("Миниатюра ${final + 2}"); scene.frames(20)
            assertEquals(final + 1, displayed.last())
            assertEquals(previousTicks + 1, feedback.size)

            // Touching an ongoing coast must stop it without a snap back to the old pager page.
            scene.pointer(PointerEventType.Press, start)
            repeat(6) { step -> scene.pointer(PointerEventType.Move, start + (end - start) * ((step + 1) / 6f)) }
            scene.pointer(PointerEventType.Release, end); scene.frames(5)
            val reverseStart = Offset(70f, strip.center.y)
            scene.pointer(PointerEventType.Press, reverseStart); scene.frames(2)
            val stopped = displayed.last()
            scene.frames(20)
            assertEquals(stopped, displayed.last(), "Holding the strip stops its inertia")
            val reverseEnd = Offset(320f, strip.center.y)
            repeat(6) { step -> scene.pointer(PointerEventType.Move, reverseStart + (reverseEnd - reverseStart) * ((step + 1) / 6f)) }
            scene.pointer(PointerEventType.Release, reverseEnd)
            imageCount = 90 // Pagination must not cancel the replacement fling.
            scene.frames(150)
            assertTrue(displayed.last() < stopped)
            assertEquals(195f, scene.label("Миниатюра ${displayed.last() + 1}").boundsInRoot.center.x, 1f)

            scene.pointer(PointerEventType.Press, start)
            repeat(6) { step -> scene.pointer(PointerEventType.Move, start + (end - start) * ((step + 1) / 6f)) }
            scene.pointer(PointerEventType.Release, end)
            val closingPage = displayed.last()
            scene.click("Закрыть фото"); scene.frames(50)
            assertEquals(closingPage, closed, "Closing during a coast must return to the photo visible at the time of closing")
        }
    }

    @Test fun shortAlbumsAndEdgesSettleInBothDirectionsAtDeviceDensity() = onUiThread {
        for (direction in listOf(LayoutDirection.Ltr, LayoutDirection.Rtl)) for (count in listOf(1, 2, 5)) {
            var selected by mutableIntStateOf(0)
            var preview: Int? = null
            ImageComposeScene(width = 1080, height = 300, density = Density(2.625f)) {
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    SauceTheme {
                        PhotoFilmstrip(List(count) { "" }, selected,
                            onPreview = { preview = it }, onSelect = { selected = it })
                    }
                }
            }.use { scene ->
                scene.frames(8)
                val strip = scene.label("Лента миниатюр").boundsInRoot
                val start = Offset(if (direction == LayoutDirection.Ltr) 950f else 130f, strip.center.y)
                val end = Offset(if (direction == LayoutDirection.Ltr) 130f else 950f, strip.center.y)
                fun fling(from: Offset, to: Offset) {
                    scene.pointer(PointerEventType.Press, from)
                    repeat(6) { step -> scene.pointer(PointerEventType.Move, from + (to - from) * ((step + 1) / 6f)) }
                    scene.pointer(PointerEventType.Release, to); scene.frames(150)
                }
                fling(start, end)
                assertEquals(count - 1, selected)
                assertEquals(540f, scene.label("Миниатюра $count").boundsInRoot.center.x, 2f)
                assertTrue(preview == null || preview in 0 until count)
                fling(end, start)
                assertEquals(0, selected)
                assertEquals(540f, scene.label("Миниатюра 1").boundsInRoot.center.x, 2f)
            }
        }
    }

    private fun onUiThread(block: () -> Unit) {
        if (SwingUtilities.isEventDispatchThread()) block()
        else try { SwingUtilities.invokeAndWait(block) } catch (e: InvocationTargetException) { throw e.targetException }
    }

    private fun ImageComposeScene.frames(count: Int) { repeat(count) { time += 20_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.label(label: String) = nodes().first { label in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
    private fun ImageComposeScene.click(label: String) {
        var node = label(label)
        while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
        node.config[SemanticsActions.OnClick].action!!.invoke()
    }
    private fun ImageComposeScene.pointer(type: PointerEventType, point: Offset) {
        time += 20_000_000L
        sendPointerEvent(type, point, type = PointerType.Touch, timeMillis = time / 1_000_000)
        render(time).close()
    }
}
