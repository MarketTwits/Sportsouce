package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartRefundRules
import com.markettwits.sportsouce.start.presentation.start.components.StartRefundRulesPanel
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartRefundRulesDesignTest {
    private val rules = StartRefundRules(318, "25 октября 2026", StartRefundRules.Source.Start, listOf(
        StartRefundRules.Rule(4, 50, 50, "Индивидуальные условия организатора для возврата участия в соревновании."),
        StartRefundRules.Rule(1, 70, 30, ""), StartRefundRules.Rule(2, 30, 14, ""), StartRefundRules.Rule(3, 0, 0, "")))

    @Test fun rulesSourcesEmptyLoadingAndRetryFitBothThemesAndLargeFonts() {
        for (dark in listOf(false, true)) for (width in listOf(320, 390, 800)) for (scale in listOf(1f, 2f)) {
            var data by mutableStateOf<StartRefundRules?>(null)
            var loading by mutableStateOf(true)
            var failed by mutableStateOf(false)
            var retries = 0
            ImageComposeScene(width, 2200, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        StartRefundRulesPanel(Modifier.padding(16.dp), data, loading, failed) { retries++ }
                    }
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.nodes().any { "Загрузка правил возврата" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
                loading = false; failed = true; scene.settle()
                val retry = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
                retry.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, retries)
                assertFalse("70%" in scene.texts(), "A failed request cannot invent default rules")
                failed = false; data = rules; scene.settle()
                assertTrue("Правила старта" in scene.texts())
                assertTrue("50%" in scene.texts())
                scene.assertTextFits(width)
                if (width == 390 && scale == 1f) scene.save("start-refund-rules-${if (dark) "dark" else "light"}")
                data = rules.copy(source = StartRefundRules.Source.Global, rules = emptyList()); scene.settle()
                assertTrue("Глобальные правила" in scene.texts())
                assertTrue("Правила возврата для этого старта не указаны." in scene.texts())
                assertFalse("50%" in scene.texts())
                scene.assertTextFits(width)
            }
        }
    }

    private var now = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(8) { now += 100_000_000L; render(now).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { t -> t.text } }
    private fun ImageComposeScene.assertTextFits(width: Int) {
        nodes().forEach { node ->
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { layout ->
                assertFalse(layout.didOverflowHeight, "Text clipped vertically: ${layout.layoutInput.text}")
                assertTrue(node.boundsInRoot.left >= 0 && node.boundsInRoot.right <= width, "Text outside pane")
                for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) - layout.getLineLeft(line) <= layout.size.width + 1f,
                    "Text clipped horizontally: ${layout.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        now += 100_000_000L
        render(now).use { image -> image.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}
