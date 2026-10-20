package com.markettwits.sportsouce.club

import androidx.compose.ui.graphics.luminance
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.sportsouce.club.common.presentation.clubColors
import com.markettwits.sportsouce.club.common.presentation.clubDirectionAccent
import kotlin.test.*

class ClubAccentTest {
    @Test fun allWebsiteAccentsKeepReadableTextInBothThemes() {
        for (dark in listOf(false, true)) {
            val colors = SubscriptionColor.entries.map { it.clubColors(dark) }
            assertEquals(4, colors.map { it.content }.distinct().size)
            for (palette in colors) {
                for (background in listOf(palette.container, sauceColors(dark).surface, sauceColors(dark).background)) {
                    val a = palette.content.luminance(); val b = background.luminance()
                    val ratio = (maxOf(a, b) + .05f) / (minOf(a, b) + .05f)
                    assertTrue(ratio >= 4.5f, "Accent text contrast is $ratio in dark=$dark")
                }
            }
        }
    }

    @Test fun scheduleAndWorkoutColorsMatchTheWebsiteSubscriptionDirections() {
        assertEquals(SubscriptionColor.GREEN, clubDirectionAccent("Беговые тренировки"))
        assertEquals(SubscriptionColor.BLUE, clubDirectionAccent("Лыжные тренировки"))
        assertEquals(SubscriptionColor.PURPLE, clubDirectionAccent("Тренировка по плаванию"))
        assertEquals(SubscriptionColor.ORANGE, clubDirectionAccent("Функциональный тренинг"))
        assertEquals(SubscriptionColor.BLUE, clubDirectionAccent("Новое направление"))
    }
}
