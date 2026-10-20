package com.markettwits.sportsouce.start.register

import androidx.compose.ui.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.sportsouce.start.register.presentation.distances.components.StartDistancesContent
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class DistanceAvailabilityDesignTest {
    @Test
    fun allAvailabilityTonesFitTheRealDistanceScreenAndOnlyAvailableDistancesCanBeSelected() {
        val base = DistinctDistance(null, null, null, null, format = "", id = 1, infiniteSlots = false,
            name = "Ретро кросс 2020", openSlots = 14, orderNumber = 1, slots = 40, stages = emptyList(), staticPrice = 500)
        val distances = listOf(base, base.copy(id = 2, name = "Ретро кросс 2021", openSlots = 0),
            base.copy(id = 3, name = "Ретро кросс 2022", openSlots = 8),
            base.copy(id = 4, name = "Открытый забег", infiniteSlots = true, openSlots = 0))
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val selected = mutableListOf<Int>()
            ImageComposeScene(width = 320, height = 2600, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    StartDistancesContent(state = StartDistancesInput(1, "Дистанции и регистрация", distances, emptyList(), false, "", StartRegType.Default),
                        onClickGoBack = {}, onClickSelectedDistance = { selected += it.id })
                }
            }.use { scene ->
                val time = System.nanoTime()
                repeat(12) { scene.render(time + it * 100_000_000L).close() }
                val nodes = scene.nodes()
                val colors = sauceColors(dark)
                scene.render(time + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                    val expected = mapOf("Осталось мест: 14" to colors.success.container,
                        "Осталось мест: 8" to colors.warning.container, "Мест нет" to colors.danger.container,
                        "Места есть" to colors.success.container)
                    for ((label, color) in expected) {
                        val badge = nodes.single { it.config.isMergingSemanticsOfDescendants &&
                            it.config.getOrNull(SemanticsProperties.Role) != Role.Button &&
                            it.config.getOrNull(SemanticsProperties.Text)?.singleOrNull()?.text == label }
                        assertTrue(badge.size.width <= 248, "Badge must stay inside the compact card at scale=$scale")
                        val bounds = badge.boundsInRoot
                        val actual = bitmap.getRGB(bounds.center.x.toInt(), bounds.top.toInt() + 2)
                        assertTrue(listOf(0, 8, 16).all { shift ->
                            abs(((actual shr shift) and 255) - ((color.toArgb() shr shift) and 255)) <= 2
                        }, "Wrong visible tone for $label, dark=$dark")
                    }
                    File("build/design-system/distance-availability-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
                nodes.forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    // The existing toolbar intentionally ellipsizes its single-line event title.
                    layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
                        assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text} at scale=$scale")
                    }
                }
                val buttons = nodes.filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Button }
                val choices = buttons.filter { node -> node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Выбрать" } }
                assertEquals(3, choices.size)
                choices.forEach { it.config[SemanticsActions.OnClick].action!!.invoke() }
                assertEquals(listOf(1, 3, 4), selected)
                assertTrue(buttons.single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == "Мест нет" } }
                    .config.contains(SemanticsProperties.Disabled))
            }
        }
    }

    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}
