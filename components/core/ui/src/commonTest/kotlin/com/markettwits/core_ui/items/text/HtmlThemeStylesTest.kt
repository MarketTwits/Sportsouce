package com.markettwits.core_ui.items.text

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HtmlThemeStylesTest {
    @Test
    fun removingFixedColorsPreservesFormattingAndMarkup() {
        val html = "<p style='color:#fff;font-weight:700;background-color:white;text-align:center'>Текст <b>старта</b></p>"
        val cleaned = removeColorStylesFromHtml(html)
        assertFalse(cleaned.contains("#fff"))
        assertFalse(cleaned.contains("background-color"))
        assertTrue(cleaned.contains("font-weight:700"))
        assertTrue(cleaned.contains("text-align:center"))
        assertTrue(cleaned.endsWith("Текст <b>старта</b></p>"))
        assertFalse(removeColorStylesFromHtml("<font color=red>Текст</font>").contains("color="))
    }
}
