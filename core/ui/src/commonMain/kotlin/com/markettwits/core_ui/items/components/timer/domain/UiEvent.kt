package com.markettwits.core_ui.items.components.timer.domain

sealed class UiEvent {
    data class OnKeyPressed(val key: Keypad): UiEvent()
}
