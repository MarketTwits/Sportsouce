import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.selfupdater.components.selft_update.components.SelfUpdateContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class SelfUpdateScreenDesignTest {
    @Test
    fun floatingBackButtonIsVisibleAndReturnsWithoutStartingUpdate() {
        var backs = 0
        var downloads = 0
        ImageComposeScene(width = 320, height = 640, density = Density(1f)) {
            SauceTheme {
                SelfUpdateContent("1.6.1", "Изменения", false, false, false, true, false, null, "", {}, { downloads++ }, {}, { backs++ })
            }
        }.use { scene ->
            repeat(3) { scene.render(System.nanoTime()).close() }
            val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
            var back = nodes.single { it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().contains("Назад") }
            while (!back.config.contains(SemanticsActions.OnClick)) back = checkNotNull(back.parent)
            assertTrue(back.boundsInRoot.top >= 0)
            assertTrue(back.boundsInRoot.bottom < 96f)
            assertTrue(back.config[SemanticsActions.OnClick].action!!.invoke())
            assertEquals(1, backs)
            assertEquals(0, downloads)
        }
    }

    @Test
    fun primaryActionStaysVisibleWithLongNotesAndLargeFont() {
        for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 640, density = Density(1f, scale)) {
                SauceTheme {
                    SelfUpdateContent("1.6.1.10422", ("Исправления и улучшения.\n\n").repeat(30),
                        false, false, false, true, false, null, "", {}, {}, {}, {})
                }
            }.use { scene ->
                repeat(3) { scene.render(System.nanoTime()).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                var action = nodes.single { it.textContains("Обновить сейчас") }
                while (!action.config.contains(SemanticsActions.OnClick)) action = checkNotNull(action.parent)
                assertTrue(action.boundsInRoot.top >= 0)
                assertTrue(action.boundsInRoot.bottom <= 640f)
                assertTrue(action.size.height >= 48)
            }
        }
    }

    @Test
    fun reopenedScreenShowsLiveDesktopProgress() {
        ImageComposeScene(width = 320, height = 800, density = Density(1f)) {
            SauceTheme {
                SelfUpdateContent("", "", false, false, false, false, false, "Обновление загружается: 10%.", "", {}, {}, {}, {},
                    downloadActive = true, downloadPercent = 10, transferPercent = 50)
            }
        }.use { scene ->
            repeat(3) { scene.render(System.nanoTime()).close() }
            val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
            assertTrue(nodes.any { it.textContains("Обновление загружается: 50%.") })
            assertTrue(nodes.mapNotNull { it.config.getOrNull(SemanticsProperties.ProgressBarRangeInfo) }.any { it.current == 0.5f })
        }
    }

    @Test
    fun desktopTransferShowsDownloadProgressDuringCheck() {
        ImageComposeScene(width = 320, height = 800, density = Density(1f)) {
            SauceTheme {
                SelfUpdateContent("", "", true, false, false, false, false, null, "", {}, {}, {}, {}, transferPercent = 50)
            }
        }.use { scene ->
            repeat(3) { scene.render(System.nanoTime()).close() }
            val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
            assertTrue(nodes.any { it.textContains("Обновление загружается: 50%.") })
            assertTrue(nodes.mapNotNull { it.config.getOrNull(SemanticsProperties.ProgressBarRangeInfo) }.any { it.current == 0.5f })
        }
    }
    @Test
    fun downloadProgressUsesAccessibleIndicatorAndStopsAtReady() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 800, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    SelfUpdateContent("", "", false, false, false, false, false, "Обновление загружается: 50%.", "", {}, {}, {}, {},
                        downloadActive = true, downloadPercent = 50)
                }
            }.use { scene ->
                repeat(3) { scene.render(System.nanoTime()).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                val progress = nodes.mapNotNull { it.config.getOrNull(SemanticsProperties.ProgressBarRangeInfo) }
                assertTrue(progress.any { it.current == 0.5f })
                assertTrue(nodes.any { it.textContains("Обновление загружается: 50%.") })
            }
        }
    }

    @Test
    fun pausedDownloadWithoutKnownSizeDoesNotAnimateAsActive() {
        ImageComposeScene(width = 320, height = 800, density = Density(1f)) {
            SauceTheme {
                SelfUpdateContent("", "", false, false, false, false, false, "Загрузка приостановлена.", "", {}, {}, {}, {},
                    downloadActive = true, downloadPercent = null, downloadPaused = true)
            }
        }.use { scene ->
            repeat(3) { scene.render(System.nanoTime()).close() }
            val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
            assertTrue(nodes.none { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) })
            assertTrue(nodes.any { it.textContains("Загрузка приостановлена.") })
        }
    }
    @Test
    fun webAndDownloadActionsMatchCapabilities() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (web in listOf(false, true)) {
            var downloads = 0
            ImageComposeScene(width = 320, height = 900, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    SelfUpdateContent("1.6.1", "", false, false, false, true, web, null, "", {}, { downloads++ }, {}, {})
                }
            }.use { scene ->
                repeat(3) { scene.render(System.nanoTime()).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                assertTrue(nodes.any { it.textContains("Новая версия 1.6.1") })
                assertEquals(!web, nodes.any { it.textContains("Обновить сейчас") })
                if (!web) {
                    var button = nodes.single { it.textContains("Обновить сейчас") }
                    while (!button.config.contains(SemanticsActions.OnClick)) button = checkNotNull(button.parent)
                    assertTrue(button.config[SemanticsActions.OnClick].action!!.invoke())
                    assertEquals(1, downloads)
                } else assertTrue(nodes.any { it.textContains("Чтобы открыть новую версию веб-приложения, обновите страницу вручную.") })
            }
        }
    }

    @Test
    fun errorStateAllowsRetry() {
        var checks = 0
        ImageComposeScene(width = 320, height = 800, density = Density(1f)) {
            SauceTheme {
                SelfUpdateContent("", "", false, false, false, false, false, "Не удалось проверить обновление.", "", {}, {}, { checks++ }, {})
            }
        }.use { scene ->
            repeat(3) { scene.render(System.nanoTime()).close() }
            val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
            var button = nodes.single { it.textContains("Проверить ещё раз") }
            while (!button.config.contains(SemanticsActions.OnClick)) button = checkNotNull(button.parent)
            assertTrue(button.config[SemanticsActions.OnClick].action!!.invoke())
            assertEquals(1, checks)
        }
    }

    private fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
    private fun SemanticsNode.textContains(value: String): Boolean =
        config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == value }
}
