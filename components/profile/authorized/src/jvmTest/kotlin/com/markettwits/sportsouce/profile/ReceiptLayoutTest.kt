package com.markettwits.sportsouce.profile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.authorized.receipts.*
import com.markettwits.sportsouce.profile.cloud.model.receipts.PaidReceipt
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ReceiptLayoutTest {
    private val receipt = PaidReceipt(42, "00000000-1111-2222-3333-444444444444",
        "Лыжная гонка патрулей «Приз Николая и Октябрины Соколовых»", "08.03.2026", "3901.50",
        participants = listOf("Анна Иванова", "Александр Александрович Константинопольский"))

    @Test fun receiptCardsFitNarrowScreensInBothThemesAndAllStatesRemainActionable() {
        for (width in listOf(320, 390)) for (scale in listOf(1f, 1.5f)) for (dark in listOf(false, true)) {
            val state = mutableStateOf(ReceiptLoadState<List<PaidReceipt>>(loading = true))
            val opened = mutableListOf<PaidReceipt>()
            var retries = 0
            ImageComposeScene(width = width, height = 1200, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        ReceiptsContent(state.value, { retries++ }, { opened += it })
                    }
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.nodes().any { "Загрузка квитанций" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
                state.value = ReceiptLoadState(data = listOf(receipt, receipt.copy(id = 43, amount = "0.00")))
                scene.settle()
                assertNotNull(scene.text("3 901,50 ₽")); assertNotNull(scene.text("0 ₽"))
                scene.click("Открыть"); assertEquals(listOf(receipt), opened)
                scene.nodes().forEach { node ->
                    val results = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(results)
                    results.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach { layout ->
                        assertFalse(layout.didOverflowHeight, "Clipped: ${layout.layoutInput.text}")
                        for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 0.5f)
                    }
                }
                if (scale == 1f) scene.save("receipts-${if (dark) "dark" else "light"}-$width")
                state.value = state.value.copy(error = "Нет соединения")
                scene.settle(); scene.click("Повторить попытку"); assertEquals(1, retries)
                assertNotNull(scene.text(receipt.startName!!), "Refresh failure must keep existing receipts")
                state.value = ReceiptLoadState(data = emptyList())
                scene.settle(); assertNotNull(scene.text("Квитанций пока нет")); assertNull(scene.text("Открыть"))
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(8) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(text: String) = nodes().firstOrNull { n ->
        n.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == text }
    }
    private fun ImageComposeScene.click(text: String) {
        var node = checkNotNull(text(text))
        while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
        assertTrue(node.size.height >= 48)
        node.config[SemanticsActions.OnClick].action!!.invoke(); settle()
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
