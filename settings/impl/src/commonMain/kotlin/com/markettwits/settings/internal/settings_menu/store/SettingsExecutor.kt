package com.markettwits.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.settings.internal.settings_menu.component.SettingsOutput
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Intent
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Label
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Message
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.State
import com.markettwits.version.ApplicationVersionManager

class SettingsExecutor(
    private val intentAction: IntentAction,
    private val versionManager: ApplicationVersionManager
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickItemMenu -> obtainMenu(intent.itemId)
        }
    }

    override fun executeAction(action: Unit) {
        dispatch(Message.CurrentVersion(versionManager.currentDistribution()))
    }

    private fun obtainMenu(itemId: Int) {
        when (itemId) {
            0 -> publish(Label.OutPut(SettingsOutput.ChangeTheme))
            1 -> publish(Label.OutPut(SettingsOutput.Appearance))
            4 -> intentAction.openWebPage(SPORT_SAUCE_GROUP)
            5 -> intentAction.openWebPage(GIT_HUB_WEB_PAGE)
            7 -> publish(Label.OutPut(SettingsOutput.CheckUpdates))
        }
    }
}

private const val GIT_HUB_WEB_PAGE = "https://github.com/MarketTwits"
private const val SPORT_SAUCE_GROUP = "https://t.me/sportsauce"
