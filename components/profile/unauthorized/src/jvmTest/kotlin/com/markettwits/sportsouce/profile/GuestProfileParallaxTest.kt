package com.markettwits.sportsouce.profile

import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileContent
import kotlinx.coroutines.Dispatchers
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestProfileParallaxTest {
    @Test fun draggingThePhotoMovesOnlyTheBackgroundAndReleaseSettlesIt() {
        for (animations in listOf(true, false)) {
            val scale = object : MotionDurationScale { override val scaleFactor = if (animations) 1f else 0f }
            ImageComposeScene(width = 390, height = 840, coroutineContext = Dispatchers.Unconfined + scale) {
                SauceTheme { UnAuthorizedProfileContent {} }
            }.use { scene ->
                var time = 1_000_000_000L
                fun frames(count: Int) { repeat(count) { time += 16_666_667L; scene.render(time).close() } }
                fun image() = scene.render(time).use { it.encodeToData()!!.use { data -> data.bytes } }
                fun touch(type: PointerEventType, y: Float) {
                    scene.sendPointerEvent(type, Offset(195f, y), timeMillis = time / 1_000_000, type = PointerType.Touch)
                    frames(1)
                }
                frames(8)
                val headingBounds = scene.nodes().single { it.config.contains(SemanticsProperties.Heading) }.boundsInRoot
                val before = image()
                touch(PointerEventType.Press, 410f)
                repeat(10) { touch(PointerEventType.Move, 410f - (it + 1) * 14f) }
                val during = image()
                assertEquals(headingBounds, scene.nodes().single { it.config.contains(SemanticsProperties.Heading) }.boundsInRoot)
                assertEquals(animations, !before.contentEquals(during), "Parallax must follow a drag and respect disabled animations")
                touch(PointerEventType.Release, 270f)
                frames(100)
                assertTrue(before.contentEquals(image()), "The photograph must settle after release")
            }
        }
    }

    @Test fun photoGestureDoesNotConsumeScrollingOrSignInOnShortScreens() {
        var clicks = 0
        ImageComposeScene(width = 320, height = 440, density = Density(1f, 2f)) {
            SauceTheme { UnAuthorizedProfileContent { clicks++ } }
        }.use { scene ->
            var time = 1_000_000_000L
            fun frames(count: Int) { repeat(count) { time += 16_666_667L; scene.render(time).close() } }
            frames(8)
            val scroll = scene.nodes().first { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
            assertTrue(scroll.config[SemanticsProperties.VerticalScrollAxisRange].maxValue() > 0)
            fun touch(type: PointerEventType, y: Float) {
                scene.sendPointerEvent(type, Offset(160f, y), timeMillis = time / 1_000_000, type = PointerType.Touch)
                frames(1)
            }
            touch(PointerEventType.Press, 350f)
            repeat(12) { touch(PointerEventType.Move, 350f - (it + 1) * 20f) }
            touch(PointerEventType.Release, 110f)
            frames(100)
            assertTrue(scroll.config[SemanticsProperties.VerticalScrollAxisRange].value() > 0, "The gesture must still scroll content")
            scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
            frames(100)
            var button = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Войти" } }
            while (!button.config.contains(SemanticsActions.OnClick)) button = button.parent!!
            assertTrue(button.boundsInRoot.top >= 0 && button.boundsInRoot.bottom <= 440)
            scene.sendPointerEvent(PointerEventType.Press, button.boundsInRoot.center)
            scene.sendPointerEvent(PointerEventType.Release, button.boundsInRoot.center)
            frames(8)
            assertEquals(1, clicks)
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
}
