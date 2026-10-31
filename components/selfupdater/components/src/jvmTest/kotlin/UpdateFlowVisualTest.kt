import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.selfupdater.components.selft_update.components.SelfUpdateContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class UpdateFlowVisualTest {
    @Test
    fun statesFitSmallPortraitLandscapeAndLargeTextWithoutClippingActions() {
        val cases = listOf("offer", "checking", "downloading", "paused", "ready", "opening", "error", "current")
        for ((width, height) in listOf(320 to 480, 640 to 360)) {
            for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (state in cases) {
                ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                    SauceTheme(darkTheme = dark) {
                        SelfUpdateContent(
                            currentVersion = if (state == "current") "" else "1.6.1.10422",
                            changes = if (state == "current") "" else ("Исправления и улучшения.\n\n").repeat(20) + "Последнее изменение",
                            isLoading = state == "checking", isSuccess = false, isFailed = state == "error",
                            isUpdatesAvailable = state == "offer", informationalOnly = false,
                            pendingMessage = when (state) {
                                "error" -> "Не удалось проверить или подготовить обновление. Проверьте подключение и повторите попытку."
                                "paused" -> "Загрузка приостановлена: 42%. Проверьте подключение."
                                "downloading" -> "Обновление загружается: 42%."
                                "ready", "opening" -> "Обновление готово к установке."
                                else -> null
                            },
                            message = "", consumed = {}, onClickStartUpdate = {}, onClickCheck = {}, onClickGoBack = {},
                            downloadActive = state in listOf("downloading", "paused"), downloadPercent = 42,
                            downloadPaused = state == "paused", readyToInstall = state in listOf("ready", "opening"),
                            installerOpening = state == "opening",
                        )
                    }
                }.use { scene ->
                    var time = System.nanoTime()
                    repeat(8) { time += 100_000_000L; scene.render(time).close() }
                    val context = "$state $width x $height dark=$dark scale=$scale"
                    val nodes = scene.nodes()
                    val back = nodes.single { it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().contains("Назад") }
                    val actions = nodes.filter { it.config.contains(SemanticsActions.OnClick) }
                    assertEquals(2, actions.size, context)
                    actions.forEach { action ->
                        assertTrue(action.boundsInRoot.top >= 0f && action.boundsInRoot.bottom <= height, context)
                        assertTrue(action.boundsInRoot.left >= 0f && action.boundsInRoot.right <= width, context)
                        assertTrue(action.touchBoundsInRoot.height >= 48f, "$context: touch target ${action.touchBoundsInRoot}")
                    }
                    nodes.filter { it.config.contains(SemanticsActions.GetTextLayoutResult) && it.boundsInRoot.height > 0 }.forEach { node ->
                        val layouts = mutableListOf<TextLayoutResult>()
                        node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                        assertTrue(layouts.none { layout ->
                            layout.didOverflowHeight || (0 until layout.lineCount).any { layout.getLineRight(it) > layout.size.width + 1f }
                        }, "$context: ${node.config.getOrNull(SemanticsProperties.Text)} ${layouts.map { layout -> listOf(layout.size, layout.didOverflowHeight, (0 until layout.lineCount).map { layout.getLineRight(it) }) }}")
                        assertFalse(node.boundsInRoot.overlaps(back.boundsInRoot), "$context: content overlaps floating back")
                    }
                    val heading = nodes.first { it.config.contains(SemanticsProperties.Heading) }
                    assertTrue(heading.boundsInRoot.height >= heading.size.height - 1f,
                        "$context: the main heading must be fully visible before scrolling")
                    if (state == "offer") {
                        nodes.single { it.config.contains(SemanticsActions.ScrollBy) }
                            .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                        repeat(15) { time += 100_000_000L; scene.render(time).close() }
                        // Markdown can finish measuring after the first scroll; exercise the
                        // updated range as a second user scroll rather than stale bounds.
                        scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                            .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                        repeat(5) { time += 100_000_000L; scene.render(time).close() }
                        val last = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Последнее изменение" } }
                        assertTrue(last.boundsInRoot.height > 0f, context)
                        assertTrue(last.boundsInRoot.bottom <= actions.maxOf { it.boundsInRoot.top }, context)
                    }
                    if (scale == 2f && dark) scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                        File("build/design-system/update-flow-$state-$width.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                    } }
                }
            }
        }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
}
