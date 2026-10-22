package com.markettwits.sportsouce.profile

import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.unauthorized.data.parseGuestWelcomeConfig
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent
import com.markettwits.sportsouce.unauthorized.presentation.components.GuestWelcomeMotion
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestRemoteContentLayoutTest {
    @Test fun publishedCopyFitsSmallPhonesAndBothThemes() {
        val variants = parseGuestWelcomeConfig(File("../../../.github/content/guest-profile.json").readText()).contents()
        for (variant in variants) for (width in listOf(320, 390)) for (dark in listOf(false, true)) {
            ImageComposeScene(width = width, height = 840) {
                SauceTheme(darkTheme = dark) { UnAuthorizedProfileContent(content = variant.copy(imageUrl = null)) {} }
            }.use { scene ->
                repeat(8) { scene.render(1_000_000_000L + it * 100_000_000L).close() }
                for (text in listOf(variant.panelTitle, variant.description)) {
                    val node = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == text } }
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                    assertTrue(layouts.none { it.didOverflowHeight || it.didOverflowWidth }, "${variant.id}: $text")
                }
                if (width == 390) scene.render(2_000_000_000L).use { frame ->
                    frame.encodeToData()!!.use { data ->
                        File("build/design-system/copy-${variant.id}-${if (dark) "dark" else "light"}.png")
                            .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                    }
                }
            }
        }
    }

    @Test fun variableRemoteTextUsesTheSameAccessibleLayoutAndAllWordsFinishRevealing() {
        val content = GuestWelcomeContent.Local.copy(id = "winter",
            headingLines = listOf("Движение объединяет", "на каждой тренировке", "и на каждом старте"),
            panelTitle = "Ваша команда и ваши новые спортивные истории",
            description = "Выбирайте соревнования, сохраняйте любимые старты, регистрируйте участников своей команды и готовьтесь к новым открытиям вместе со Спорт Союзом.")
        for ((width, height, scale) in listOf(Triple(390, 840, 1f), Triple(320, 560, 2f), Triple(800, 360, 1f))) {
            ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                SauceTheme { UnAuthorizedProfileContent(content = content) {} }
            }.use { scene ->
                repeat(8) { scene.render(1_000_000_000L + it * 100_000_000L).close() }
                val nodes = scene.nodes()
                assertEquals(content.headingLines.joinToString("\n"),
                    nodes.single { it.config.contains(SemanticsProperties.Heading) }.config[SemanticsProperties.Text].single().text)
                for (text in listOf(content.panelTitle, content.description)) {
                    val node = nodes.single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == text } }
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                    assertTrue(layouts.none { it.didOverflowHeight || it.didOverflowWidth })
                }
                val scroll = nodes.first { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                repeat(15) { scene.render(2_000_000_000L + it * 100_000_000L).close() }
                val signIn = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == "Войти" } }
                assertTrue(signIn.boundsInRoot.bottom <= height)
            }
        }
        val longest = GuestWelcomeMotion(1f, List(3) { "Я".repeat(28) })
        repeat(3) { line -> repeat(28) { character -> assertEquals(1f, longest.characterAlpha(line, character)) } }
        repeat(90) { word ->
            assertEquals(1f, longest.titleWordAlpha(word, 90))
            assertEquals(1f, longest.descriptionWordAlpha(word, 90))
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
}
