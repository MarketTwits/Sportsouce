package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlin.math.abs
import kotlin.test.*
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.presentation.start.components.StartOrganizers

/** Compare the actual supporting text to the same text rendered without an ancestor shape clip. */
@OptIn(ExperimentalComposeUiApi::class)
class OrganizerTextClippingTest {
    @Test fun supportingGlyphsAtTheLeftEdgeMatchTheirUnclippedRaster() {
        val person = Organizer(1, "", false, name = "РЖД", phone = null, socialNetworks = emptyList(), startId = 315)
        val label = "Контакты не указаны"
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var reference by mutableStateOf<TextLayoutResult?>(null)
            var bounds = Rect.Zero
            ImageComposeScene(width = 390, height = 600, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    val density = LocalDensity.current
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.surface)) {
                        val layout = reference
                        if (layout == null) StartOrganizers(organizer = listOf(person), onClickUrl = {}, onClickPhone = {})
                        else Text(layout.layoutInput.text,
                            Modifier.offset { IntOffset(bounds.left.toInt(), bounds.top.toInt()) }
                                .width(with(density) { bounds.width.toDp() }),
                            style = layout.layoutInput.style, maxLines = layout.layoutInput.maxLines,
                            overflow = layout.layoutInput.overflow, softWrap = layout.layoutInput.softWrap)
                    }
                }
            }.use { scene ->
                var time = System.nanoTime()
                fun settle() { repeat(12) { time += 100_000_000L; scene.render(time).close() } }
                fun pixels() = scene.render(time).use { it.toComposeImageBitmap().toPixelMap() }
                fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
                settle()
                val text = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.single {
                    it.config.contains(SemanticsActions.GetTextLayoutResult) &&
                        it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { value -> value.text == label }
                }
                bounds = text.boundsInRoot
                val actual = pixels()
                val layouts = mutableListOf<TextLayoutResult>()
                text.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                reference = layouts.single()
                settle()
                val expected = pixels()
                var altered = 0
                for (y in bounds.top.toInt() until bounds.bottom.toInt())
                    for (x in bounds.left.toInt() until bounds.left.toInt() + 16) {
                        val a = actual[x, y]; val e = expected[x, y]
                        if (abs(a.red - e.red) + abs(a.green - e.green) + abs(a.blue - e.blue) > .05f) altered++
                    }
                assertEquals(0, altered, "The first glyph must not lose pixels to a rounded ancestor: dark=$dark scale=$scale")
            }
        }
    }
}
