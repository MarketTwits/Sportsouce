package com.markettwits.settings.internal.change_theme.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.settings.internal.change_theme.components.ColorThemeUi


interface ChangeThemeStore :
    Store<ChangeThemeStore.Intent, ChangeThemeStore.State, ChangeThemeStore.Label> {
    data class State(val items: List<ColorThemeUi>)

    sealed interface Intent {
        data object Dismiss : Intent
        data class OnClickTheme(val themeUi: ColorThemeUi) : Intent
    }

    sealed interface Message {
        data class ThemeMenu(val themeUi: List<ColorThemeUi>) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
    }

}
