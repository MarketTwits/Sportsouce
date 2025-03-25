package com.markettwits.core.theme.component

import com.markettwits.sportsouce.settings.api.api.params.ColorTheme
import kotlinx.coroutines.flow.StateFlow

interface ThemeComponent {
    fun getAppTheme(): StateFlow<ColorTheme>
}