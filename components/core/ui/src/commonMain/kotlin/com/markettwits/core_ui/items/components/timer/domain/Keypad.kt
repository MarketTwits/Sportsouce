package com.markettwits.core_ui.items.components.timer.domain

sealed class Keypad(
    val value: String,
) {
    data object Key1: Keypad("1")
    data object Key2: Keypad("2")
    data object Key3: Keypad("3")
    data object Key4: Keypad("4")
    data object Key5: Keypad("5")
    data object Key6: Keypad("6")
    data object Key7: Keypad("7")
    data object Key8: Keypad("8")
    data object Key9: Keypad("9")
    data object Key0: Keypad("0")
    data object Key00: Keypad("00")
    data object KeyDelete: Keypad("x")
    data object KeyApply : Keypad("")
}