package com.markettwits.sportsouce.settings.internal.settings_menu.component

sealed interface SettingsOutput {
    fun handleOutput(handleSettingsMenu: HandleSettingsMenu)

    data object ChangeTheme : SettingsOutput {
        override fun handleOutput(handleSettingsMenu: HandleSettingsMenu) {
            handleSettingsMenu.openChangeThemeScreen()
        }
    }

    data object CheckUpdates : SettingsOutput {
        override fun handleOutput(handleSettingsMenu: HandleSettingsMenu) {
            handleSettingsMenu.openCheckUpdatesScreen()
        }
    }
}