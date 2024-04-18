package com.markettwits.theme.theme.component

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.settings.api.api.SettingsRepository
import com.markettwits.settings.api.api.params.ColorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThemeComponentFeature(
    private val settingsRepository: SettingsRepository
) : InstanceKeeper.Instance, ThemeComponent {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val theme = MutableStateFlow<ColorTheme>(ColorTheme.System)
    override fun getAppTheme(): StateFlow<ColorTheme> = theme

    init {
        launch()
    }

    private fun launch() {
        scope.launch {
            settingsRepository.observeSettings()
                .map { it.theme }
                .collect {
                    theme.value = it
                }
        }
    }
}