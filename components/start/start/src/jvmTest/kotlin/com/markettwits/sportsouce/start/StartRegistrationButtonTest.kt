package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.start.components.StartRegistrationButton
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartRegistrationButtonTest {
    @Test
    fun previewLoadingAndFailureCannotRegisterAndTheLabelDoesNotJump() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var loading by mutableStateOf(false)
            var partial by mutableStateOf(true)
            var error by mutableStateOf(false)
            var clicks = 0
            ImageComposeScene(width = 320, height = 220, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        StartRegistrationButton(startStatus = StartItem.StartStatus(3, ""), isLoading = loading,
                            isPartialData = partial, hasError = error, onClickRegistration = { clicks++ })
                    }
                }
            }.use { scene ->
                scene.settle()
                val bounds = scene.button().boundsInRoot
                fun assertStableBounds() {
                    repeat(6) {
                        scene.frameBytes(50_000_000L)
                        assertEquals(bounds, scene.button().boundsInRoot, "Button width, height and position must not change between states")
                    }
                }
                fun assertBlocked(expectLoading: Boolean) {
                    assertTrue(scene.button().config.contains(SemanticsProperties.Disabled))
                    if (expectLoading) assertEquals("Загрузка данных старта", scene.button().config[SemanticsProperties.StateDescription])
                    else assertNull(scene.button().config.getOrNull(SemanticsProperties.StateDescription))
                    scene.button().config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
                    assertEquals(0, clicks)
                    assertEquals(bounds, scene.button().boundsInRoot)
                    assertFalse(scene.nodes().any { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) }, "No spinner should be shown")
                }
                assertBlocked(expectLoading = true) // Preview exists before the request coroutine starts.
                loading = true; assertStableBounds(); assertBlocked(expectLoading = true)
                scene.save("start-registration-loading-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                loading = false; error = true; assertStableBounds(); assertBlocked(expectLoading = false)
                partial = false; assertStableBounds(); assertBlocked(expectLoading = false)
                error = false; assertStableBounds()
                assertFalse(scene.button().config.contains(SemanticsProperties.Disabled))
                assertEquals(bounds, scene.button().boundsInRoot)
                val label = scene.nodes().single { it.config.contains(SemanticsActions.GetTextLayoutResult) }
                val layouts = mutableListOf<TextLayoutResult>()
                label.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                assertEquals(16.sp, layouts.single().layoutInput.style.fontSize)
                assertFalse(layouts.single().hasVisualOverflow)
                scene.save("start-registration-ready-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                scene.button().config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(1, clicks)
                loading = true; assertStableBounds()
                scene.button().config[SemanticsActions.OnClick].action!!.invoke(); assertEquals(1, clicks, "Refresh must also block registration")
            }
        }
    }

    @Test
    fun shimmerMovesOnlyWhileWaitingAndStopsWhenTheButtonIsReady() {
        for (dark in listOf(false, true)) {
            var loading by mutableStateOf(true)
            ImageComposeScene(width = 320, height = 120) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        StartRegistrationButton(startStatus = StartItem.StartStatus(3, ""), isLoading = loading, onClickRegistration = {})
                    }
                }
            }.use { scene ->
                scene.settle()
                val first = scene.frameBytes(250_000_000L)
                val movingFrames = List(8) { scene.frameBytes(100_000_000L) }
                assertTrue(movingFrames.any { !first.contentEquals(it) }, "Waiting button should have a moving shimmer")
                loading = false; scene.settle()
                assertContentEquals(scene.frameBytes(250_000_000L), scene.frameBytes(500_000_000L), "Ready button must stop shimmering")
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.button() = nodes().single { it.config.contains(SemanticsActions.OnClick) }
    private fun ImageComposeScene.frameBytes(step: Long): ByteArray {
        time += step
        return render(time).use { img -> img.encodeToData()!!.use { it.bytes } }
    }
    private fun ImageComposeScene.save(name: String) {
        File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(frameBytes(100_000_000L)) }
    }
}
