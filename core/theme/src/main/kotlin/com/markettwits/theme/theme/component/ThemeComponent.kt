package com.markettwits.theme.theme.component

import com.markettwits.settings.api.api.params.ColorTheme
import kotlinx.coroutines.flow.StateFlow

interface ThemeComponent {
    fun getAppTheme(): StateFlow<ColorTheme>
}