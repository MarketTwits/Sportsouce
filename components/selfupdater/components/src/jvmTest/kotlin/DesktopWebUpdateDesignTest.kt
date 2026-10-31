import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.selfupdater.models.UpdateTarget
import com.markettwits.selfupdater.components.selft_update.components.SelfUpdateContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class DesktopWebUpdateDesignTest {
    @Test
    fun desktopDownloadsAndOpensTheFileFromTheSameScreen() {
        for (dark in listOf(false, true)) for ((width, height) in listOf(1280 to 860, 720 to 500)) {
            for (ready in listOf(false, true)) {
                var downloads = 0
                var opens = 0
                ImageComposeScene(width = width, height = height) {
                    SauceTheme(darkTheme = dark) {
                        SelfUpdateContent("1.6.1", "Изменения", false, false, false, !ready, false,
                            if (ready) "Обновление готово к установке." else null, "", {}, { downloads++ }, {}, {},
                            readyToInstall = ready, onClickInstall = { opens++ }, updateTarget = UpdateTarget.Desktop)
                    }
                }.use { scene ->
                    scene.settle()
                    val label = if (ready) "Открыть файл обновления" else "Скачать обновление"
                    val nodes = scene.nodes()
                    assertTrue(nodes.none { it.textContains("Установку подтвердит Android.") })
                    var action = nodes.single { it.textContains(label) }
                    while (!action.config.contains(SemanticsActions.OnClick)) action = checkNotNull(action.parent)
                    assertTrue(action.boundsInRoot.bottom <= height)
                    assertTrue(action.config[SemanticsActions.OnClick].action!!.invoke())
                    assertEquals(if (ready) 0 else 1, downloads)
                    assertEquals(if (ready) 1 else 0, opens)
                    scene.save("desktop-update-${if (ready) "ready" else "offer"}-$width-${if (dark) "dark" else "light"}")
                }
            }
        }
    }
    @Test
    fun webOffersOnlyManualPageRefreshAndAllowsRetryAfterFailure() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (failed in listOf(false, true)) {
            var downloads = 0
            var checks = 0
            ImageComposeScene(width = 900, height = 700, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    SelfUpdateContent("1.6.1", "Что изменилось", false, false, failed, !failed, true,
                        if (failed) "Не удалось проверить новую версию сайта." else null, "", {},
                        { downloads++ }, { checks++ }, {}, updateTarget = UpdateTarget.Web)
                }
            }.use { scene ->
                scene.settle()
                val nodes = scene.nodes()
                assertFalse(nodes.any { it.textContains("Установить") || it.textContains("Скачать обновление") || it.textContains("Обновить сейчас") })
                if (failed) {
                    var action = nodes.single { it.textContains("Проверить ещё раз") }
                    while (!action.config.contains(SemanticsActions.OnClick)) action = checkNotNull(action.parent)
                    action.config[SemanticsActions.OnClick].action!!.invoke()
                    assertEquals(1, checks)
                } else assertTrue(nodes.any { it.textContains("Чтобы открыть новую версию веб-приложения, обновите страницу вручную.") })
                assertEquals(0, downloads)
                scene.save("web-update-${if (failed) "error" else "offer"}-${if (dark) "dark" else "light"}-$scale")
            }
        }
    }
    @Test
    fun webReleaseNotesScrollClearOfThePinnedRefreshInstructionAtSmallAndLargeFontSizes() {
        for ((width, height, scale) in listOf(Triple(360, 640, 2f), Triple(900, 400, 1f), Triple(1280, 860, 1f))) {
            val changes = "## Исправления\n\n" + (1..20).joinToString("\n\n") {
                "Изменение $it: улучшена работа приложения и отображение данных."
            } + "\n\nWEB_CHANGELOG_END"
            ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                SauceTheme {
                    SelfUpdateContent("1.6.1", changes, false, false, false, true, true,
                        null, "", {}, {}, {}, {}, updateTarget = UpdateTarget.Web)
                }
            }.use { scene ->
                scene.settle()
                scene.awaitText("WEB_CHANGELOG_END")
                scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 100000f)
                scene.settle()
                val end = scene.nodes().single { it.textContains("WEB_CHANGELOG_END") }
                val instruction = scene.nodes().single {
                    it.textContains("Чтобы открыть новую версию веб-приложения, обновите страницу вручную.")
                }
                assertTrue(end.boundsInRoot.height > 0f)
                assertTrue(end.boundsInRoot.bottom <= instruction.boundsInRoot.top,
                    "The entire changelog must be reachable above the pinned refresh instruction")
                scene.save("web-update-scroll-end-$width-$scale")
                scene.nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }.forEach {
                    val layouts = mutableListOf<TextLayoutResult>()
                    it.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                    val layout = layouts.single()
                    val message = "Web update text must fit at $width×$height, font $scale: ${layout.layoutInput.text.text}"
                    if (layout.layoutInput.text.text == "Исправления") {
                        assertEquals(1, layout.lineCount,
                            "A release-note heading must not split inside the word on a narrow screen")
                    }
                    assertFalse(layout.didOverflowHeight, message)
                    // Text's intrinsic width can round down to an integer pixel in a Row.
                    assertTrue((0 until layout.lineCount).all { line ->
                        layout.getLineRight(line) <= layout.size.width + 1f
                    }, message)
                }
            }
        }
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.awaitText(text: String) {
        // Markdown parsing completes asynchronously; frame-clock time alone doesn't await it.
        val deadline = System.nanoTime() + 5_000_000_000L
        while (nodes().none { it.textContains(text) } && System.nanoTime() < deadline) {
            time += 16_000_000L
            render(time).close()
            Thread.sleep(5)
        }
        assertTrue(nodes().any { it.textContains(text) }, "Markdown content did not finish rendering: $text")
    }
    private fun ImageComposeScene.settle() { repeat(15) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun SemanticsNode.textContains(text: String) = config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == text }
    private fun ImageComposeScene.save(name: String) {
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
