package com.markettwits.core.theme.components

import com.markettwits.core_ui.items.theme.sauceColors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class LegacyColorsTest {
    @Test
    fun compatibilityAdapterPreservesSurfaceErrorAndFixedRolesInBothThemes() {
        for (dark in listOf(false, true)) {
            val palette = sauceColors(dark)
            val scheme = palette.forLegacyScreens()
            assertEquals(palette.surface, scheme.primary, "old backgrounds must stay neutral")
            assertEquals(palette.action, scheme.secondary, "old actions use the new action pair")
            assertEquals(palette.onAction, scheme.onSecondary)
            assertEquals(palette.textPrimary, scheme.tertiary, "old title/text role stays readable")
            assertEquals(palette.material.surface, scheme.surface)
            assertEquals(palette.material.onSurface, scheme.onSurface)
            assertEquals(palette.material.surfaceContainerHigh, scheme.surfaceContainerHigh)
            assertEquals(palette.material.surfaceContainerLow, scheme.surfaceContainerLow)
            assertEquals(palette.material.surfaceDim, scheme.surfaceDim)
            assertEquals(palette.material.error, scheme.error)
            assertEquals(palette.material.onError, scheme.onError)
            assertEquals(palette.material.errorContainer, scheme.errorContainer)
            assertEquals(palette.material.onErrorContainer, scheme.onErrorContainer)
            assertEquals(palette.material.primaryFixed, scheme.primaryFixed)
            assertEquals(palette.material.surfaceTint, scheme.surfaceTint)
            assertEquals(palette.material.scrim, scheme.scrim)
        }
        assertNotEquals(LightColorScheme.surface, DarkColorScheme.surface)
        assertNotEquals(LightColorScheme.errorContainer, DarkColorScheme.errorContainer)
    }
}
