package com.markettwits.core_ui.items.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SauceColorsTest {
    @Test
    fun readingAndActionPairsHaveAccessibleContrastInBothThemes() {
        for (dark in listOf(false, true)) {
            val colors = sauceColors(dark)
            val pairs = listOf(
                "primary text" to (colors.textPrimary to colors.surface),
                "secondary text" to (colors.textSecondary to colors.surface),
                "muted text" to (colors.textMuted to colors.surface),
                "raised text" to (colors.textPrimary to colors.surfaceRaised),
                "action" to (colors.onAction to colors.action),
                "soft action" to (colors.onActionSoft to colors.actionSoft),
                "brand" to (colors.brand to colors.background),
                "brand container" to (colors.onBrandContainer to colors.brandContainer),
            ) + listOf(colors.success, colors.warning, colors.danger, colors.neutral).map {
                "status" to (it.content to it.container)
            }
            for ((role, pair) in pairs) {
                assertTrue(contrast(pair.first, pair.second) >= 4.5f, "$role in dark=$dark")
            }
            assertTrue(contrast(colors.action, colors.surface) >= 3f, "action boundary in dark=$dark")
            assertTrue(contrast(colors.controlBorder, colors.surface) >= 3f, "field boundary in dark=$dark")
        }
    }

    @Test
    fun darkThemeAdaptsBrandAndActionsWithoutUsingThemAsSurfaces() {
        assertNotEquals(LightSauceColors.action, DarkSauceColors.action)
        assertNotEquals(LightSauceColors.brand, DarkSauceColors.brand)
        for (colors in listOf(LightSauceColors, DarkSauceColors)) {
            assertNotEquals(colors.brand, colors.action)
            assertNotEquals(colors.action, colors.surface)
            assertNotEquals(colors.background, colors.surface)
            assertNotEquals(colors.success.container, colors.danger.container)
        }
    }

    private fun contrast(a: Color, b: Color): Float {
        val lighter = maxOf(a.luminance(), b.luminance())
        val darker = minOf(a.luminance(), b.luminance())
        return (lighter + 0.05f) / (darker + 0.05f)
    }
}
