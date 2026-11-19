package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ActionRowAlignmentTest {
    @Test
    fun visibleLeadingIconAndChevronStayCenteredInTheWholeAction() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (subtitle in listOf<String?>(null, "Длинная подпись участника в несколько строк")) {
            var leading = 0; var trailing = 0
            ImageComposeScene(width = 390, height = 650, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    leading = SauceTheme.colors.textSecondary.toArgb()
                    trailing = SauceTheme.colors.textMuted.toArgb()
                    SauceActionRow(title = "Участники", count = "12345", subtitle = subtitle, icon = Icons.Outlined.Group, onClick = {})
                }
            }.use { scene ->
                var time = System.nanoTime()
                repeat(8) { time += 100_000_000L; scene.render(time).close() }
                val target = scene.semanticsOwners.single().rootSemanticsNode.children.single { it.config.contains(SemanticsActions.OnClick) }
                val bounds = target.boundsInRoot
                assertTrue(bounds.height >= 48 && bounds.width >= 48)
                scene.render(time + 100_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                    fun iconCenterY(color: Int, left: Int, right: Int): Float {
                        val pixels = (bounds.top.toInt() until bounds.bottom.toInt()).filter { y ->
                            (left until right).any { x ->
                                val pixel = bitmap.getRGB(x, y)
                                listOf(0, 8, 16).all { shift -> abs(((pixel shr shift) and 255) - ((color shr shift) and 255)) <= 4 }
                            }
                        }
                        assertTrue(pixels.isNotEmpty(), "Visible icon must be rendered")
                        return (pixels.first() + pixels.last() + 1) / 2f
                    }
                    assertEquals(bounds.center.y, iconCenterY(leading, bounds.left.toInt(), bounds.left.toInt() + 40), 1.25f,
                        "Leading icon drifts from the row center, scale=$scale, subtitle=$subtitle")
                    assertEquals(bounds.center.y, iconCenterY(trailing, bounds.right.toInt() - 40, bounds.right.toInt()), 1.25f,
                        "Chevron drifts from the row center, scale=$scale, subtitle=$subtitle")
                } }
            }
        }
    }
}
