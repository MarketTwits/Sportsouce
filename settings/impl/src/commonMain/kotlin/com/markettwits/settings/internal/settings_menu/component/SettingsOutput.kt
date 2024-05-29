package com.markettwits.settings.internal.settings_menu.component

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

    data object Appearance : SettingsOutput {
        override fun handleOutput(handleSettingsMenu: HandleSettingsMenu) {
            handleSettingsMenu.openAppearanceContent()
        }
    }
}