package com.markettwits.sportsouce.settings.internal.change_theme.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.settings.api.api.MutableSettingsRepository
import com.markettwits.sportsouce.settings.api.api.params.ColorTheme
import com.markettwits.sportsouce.settings.internal.change_theme.components.ColorThemeUi
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore.Intent
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore.Label
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore.Message
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore.State
import kotlinx.coroutines.launch

class ChangeThemeExecutor(
    private val settingsRepository: MutableSettingsRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickTheme -> changeSettings(intent.themeUi)
        }
    }

    override fun executeAction(action: Unit) {
        observeSettings()
    }



    private fun changeSettings(colorThemeUi: ColorThemeUi) {
        scope.launch {
            val selectedTheme = when (colorThemeUi.id) {
                0 -> ColorTheme.DarkTheme
                1 -> ColorTheme.LightTheme
                2 -> ColorTheme.System
                else -> error("unknown theme id ${colorThemeUi.id}")
            }
            val settings = settingsRepository.fetchSettings()
            settingsRepository.updateSettings(settings.copy(theme = selectedTheme))
            publish(Label.Dismiss)
        }
    }

    private fun observeSettings() {
        scope.launch {
            val settings = settingsRepository.fetchSettings()
            val themeList = listOf(
                ColorThemeUi(
                    0,
                    "Темная тема",
                    settings.theme == ColorTheme.DarkTheme
                ),
                ColorThemeUi(
                    1,
                    "Светлая тема",
                    settings.theme == ColorTheme.LightTheme
                ),
                ColorThemeUi(
                    2,
                    "Как в системе",
                    settings.theme == ColorTheme.System
                )
            )
            dispatch(Message.ThemeMenu(themeList))
        }
    }
}
