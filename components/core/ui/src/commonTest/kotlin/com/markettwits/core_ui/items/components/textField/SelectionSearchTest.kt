package com.markettwits.core_ui.items.components.textField

import kotlin.test.Test
import kotlin.test.assertEquals

class SelectionSearchTest {
    @Test
    fun spacesCaseAndRussianYoDoNotHideMatchingOptions() {
        val options = listOf("Нижний Новгород", "Новосибирская область", "Озёрный", "Москва")
        assertEquals(listOf("Нижний Новгород"), filterSelectionItems(options, " нижний\u00a0НОВГОРОД "))
        assertEquals(listOf("Новосибирская область"), filterSelectionItems(options, "новосибирская обл"))
        assertEquals(listOf("Озёрный"), filterSelectionItems(options, "озерный"))
        assertEquals(emptyList(), filterSelectionItems(options, "нет такого места"))
    }
}
