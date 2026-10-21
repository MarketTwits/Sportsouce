package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.components.snackbar.*
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class SnackbarDesignTest {
    @Test fun serviceMessagesWrapAndRemainDismissibleInBothThemes() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (tone in SauceSnackbarTone.entries) {
            val state = SnackbarHostState()
            ImageComposeScene(width = 360, height = 600, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        SauceSnackbarHost(state, tone = tone)
                    }
                    LaunchedEffect(Unit) {
                        state.showSnackbar("Не удалось отправить данные. Проверьте подключение и попробуйте ещё раз.",
                            withDismissAction = true, duration = SnackbarDuration.Indefinite)
                    }
                }
            }.use { scene ->
                repeat(16) { scene.render(System.nanoTime() + it * 100_000_000L).close() }
                fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
                val nodes = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                assertTrue(nodes.any { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text.contains("Проверьте подключение") } })
                val dismiss = nodes.first { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(dismiss.size.width >= 48 && dismiss.size.height >= 48)
                nodes.forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped snackbar at scale=$scale") }
                }
                scene.render(System.nanoTime() + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/snackbar-${tone.name.lowercase()}-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
                dismiss.config[SemanticsActions.OnClick].action!!.invoke()
                assertNull(state.currentSnackbarData)
            }
        }
    }
}
