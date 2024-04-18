package com.markettwits.theme.theme.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.settings.api.api.params.ColorTheme
import com.markettwits.settings.api.api.settingsModule
import kotlinx.coroutines.flow.StateFlow

class ThemeComponentBase(
    private val componentContext: ComponentContext,
) : ThemeComponent, ComponentContext by componentContext {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }
    private val scope = koinContext.getOrCreateKoinScope(
        listOf(settingsModule)
    )

    private val feature = instanceKeeper.getOrCreate {
        ThemeComponentFeature(scope.get())
    }

    override fun getAppTheme(): StateFlow<ColorTheme> = feature.getAppTheme()
}