package com.markettwits.settings.internal.appearance.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.settings.api.api.MutableSettingsRepository
import com.markettwits.settings.api.api.params.BottomPanel
import com.markettwits.settings.internal.appearance.components.BottomBarUi
import com.markettwits.settings.internal.appearance.store.AppearanceStore.Intent
import com.markettwits.settings.internal.appearance.store.AppearanceStore.Label
import com.markettwits.settings.internal.appearance.store.AppearanceStore.Message
import com.markettwits.settings.internal.appearance.store.AppearanceStore.State
import kotlinx.coroutines.launch

internal class AppearanceExecutor(
    private val settingsRepository: MutableSettingsRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickItem -> changeSettings(intent.item)
        }
    }

    override fun executeAction(action: Unit) {
        observeSettings()
    }

    private fun changeSettings(bottomBarUi: BottomBarUi) {
        scope.launch {
            val bottomPanel = when (bottomBarUi.id) {
                0 -> BottomPanel(true)
                1 -> BottomPanel(false)
                else -> error("unknown theme id ${bottomBarUi.id}")
            }
            val settings = settingsRepository.fetchSettings()
            settingsRepository.updateSettings(settings.copy(bottomPanel = bottomPanel))
            publish(Label.GoBack)
        }
    }

    private fun observeSettings() {
        scope.launch {
            val settings = settingsRepository.fetchSettings()
            val isCompact = settings.bottomPanel.isCompact
            val items = listOf(
                BottomBarUi(
                    0,
                    isSelected = isCompact,
                    title = "Компактный"
                ),
                BottomBarUi(
                    1,
                    isSelected = !isCompact,
                    title = "Полный"
                )
            )
            dispatch(Message.ThemeMenu(items))
        }
    }
}
