import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.impl.composable.ComposableInAppNotification
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class UpdateNotificationDesignTest {
    @Test
    fun reviewNoticeStaysCompactAndShowsHumanReadableVersion() {
        var height = 0
        ImageComposeScene(width = 411, height = 500, density = Density(1f)) {
            Palette(false) {
                ComposableInAppNotification(
                    InAppNotification.SelfUpdateReady({}, "1.6.1.10422", ""),
                    onNotificationOpen = {}, onNotificationHidden = {},
                    modifier = Modifier.onGloballyPositioned { height = it.size.height },
                )
            }
        }.use { scene ->
            repeat(3) { scene.render(System.nanoTime()).close() }
            assertTrue(height <= 48, "The notice should stay within one compact touch target, got $height pixels")
            val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
            assertTrue(nodes.any { it.textContains("Обновить Sportsauce") })
            assertTrue(nodes.none { it.textContains("Версия 1.6.1.10422") })
            assertTrue(nodes.none { it.textContains("Версия 1.6.1.10422") })
        }
    }

    @Test
    fun notificationRespectsHostWidthAndKeepsActionsWithinBounds() {
        for (scale in listOf(1f, 2f)) {
            var width = 0
            val notification = InAppNotification.SelfUpdateReady({}, "1.6.1", "")
            ImageComposeScene(width = 320, height = 700, density = Density(1f, scale)) {
                Palette(false) {
                    ComposableInAppNotification(notification, onNotificationOpen = {}, onNotificationHidden = {},
                        modifier = Modifier.width(240.dp).onGloballyPositioned { width = it.size.width })
                }
            }.use { scene ->
                repeat(3) { scene.render(System.nanoTime()).close() }
                assertEquals(240, width)
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                for (label in listOf("Обновить Sportsauce")) {
                    var node = nodes.single { it.textContains(label) || it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().contains(label) }
                    while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
                    assertTrue(node.boundsInRoot.right <= 240f, "$label is outside the notification")
                    assertTrue(node.size.height >= 48)
                }
            }
        }
    }

    @Test
    fun desktopActionsDescribeTheActualInstallerType() {
        for ((diskImage, title) in listOf(true to "Открыть образ установки", false to "Открыть установщик")) {
            val notification = InAppNotification.SelfUpdateReady(
                action = {}, actualVersion = "1.6.1", description = "", readyToInstall = true,
                desktopInstaller = true, desktopDiskImage = diskImage,
            )
            ImageComposeScene(width = 320, height = 800, density = Density(1f, 2f)) {
                Palette(true) { ComposableInAppNotification(notification, onNotificationOpen = {}, onNotificationHidden = {}) }
            }.use { scene ->
                repeat(3) { scene.render(System.nanoTime()).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                assertTrue(nodes.any { it.textContains(title) })
                assertTrue(nodes.none { it.textContains("Открыть папку") })
            }
        }
    }
    @Test
    fun downloadAndFailureMessagesAreVisibleAndDismissible() {
        val cases = listOf(
            InAppNotification.SelfUpdateStarted to "Загрузка обновления…",
            InAppNotification.SelfUpdateError(IllegalStateException("technical detail")) to "Не удалось подготовить обновление",
        )
        for ((notification, title) in cases) {
            var dismissed = 0
            var opened = 0
            ImageComposeScene(width = 320, height = 800, density = Density(1f, 2f)) {
                Palette(true) {
                    ComposableInAppNotification(notification, onNotificationOpen = { opened++ }, onNotificationHidden = { dismissed++ })
                }
            }.use { scene ->
                repeat(5) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                assertTrue(nodes.any { it.textContains(title) })
                assertTrue(nodes.none { it.textContains("technical detail") })
                val actions = nodes.filter { it.config.contains(SemanticsActions.OnClick) }
                if (notification is InAppNotification.SelfUpdateError) {
                    assertTrue(actions.single().size.height >= 48)
                    assertTrue(actions.single().config[SemanticsActions.OnClick].action!!.invoke())
                    assertEquals(1, dismissed)
                } else {
                    assertTrue(actions.single().config[SemanticsActions.OnClick].action!!.invoke())
                    assertEquals(1, opened)
                    assertEquals(0, dismissed)
                }

            }
        }
    }

    @Test
    fun webNoticeHasAccessibleActionsAndDoesNotOfferInstallation() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var opened = 0
            var dismissed = 0
            val notification = InAppNotification.SelfUpdateReady(
                action = {}, actualVersion = "1.6.1", description = "", informationalOnly = true,
            )
            ImageComposeScene(width = 320, height = 900, density = Density(1f, scale)) {
                Palette(dark) {
                    ComposableInAppNotification(
                        notification = notification,
                        onNotificationOpen = { opened++ },
                        onNotificationHidden = { dismissed++ },
                    )
                }
            }.use { scene ->
                repeat(5) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                assertTrue(nodes.any { it.textContains("Доступно обновление сайта") })
                assertTrue(nodes.none { it.textContains("Установить") })
                val action = nodes.single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(action.size.height >= 48)
                assertTrue(action.config[SemanticsActions.OnClick].action!!.invoke())
                assertEquals(1, opened)
                assertEquals(0, dismissed)
                scene.render(System.nanoTime()).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/update-notice-${if (dark) "dark" else "light"}-$scale.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
            }
        }
    }

    @Composable
    private fun Palette(dark: Boolean, content: @Composable () -> Unit) = SauceTheme(darkTheme = dark, content = content)

    private fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
    private fun SemanticsNode.textContains(value: String): Boolean =
        config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == value }
}
