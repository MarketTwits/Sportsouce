package com.markettwits.sportsouce.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.cahce.CacheManager
import com.markettwits.sportsouce.settings.internal.settings_menu.component.SettingsOutput
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore.*
import com.markettwits.version.ApplicationVersionManager
import kotlinx.coroutines.launch

class SettingsExecutor(
    private val intentAction: IntentAction,
    private val versionManager: ApplicationVersionManager,
    private val cacheManager: CacheManager,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickItemMenu -> obtainMenu(intent.itemId)
            is Intent.ShowClearCacheDialog -> dispatch(Message.ShowClearCacheDialog(true))
            is Intent.HideClearCacheDialog -> dispatch(Message.ShowClearCacheDialog(false))
            is Intent.ClearCache -> clearCache()
        }
    }

    override fun executeAction(action: Unit) {
        dispatch(Message.CurrentVersion(versionManager.currentDistribution()))
        loadCacheSize()
    }

    private fun loadCacheSize() {
        scope.launch {
            try {
                val cacheSize = cacheManager.getCacheSize()
                val formattedSize = cacheManager.formatCacheSize(cacheSize)
                dispatch(Message.CacheSizeUpdated(formattedSize))
            } catch (e: Exception) {
                dispatch(Message.CacheSizeUpdated("Ошибка"))
            }
        }
    }

    private fun clearCache() {
        scope.launch {
            try {
                val success = cacheManager.clearCache()
                if (success) {
                    dispatch(Message.CacheSizeUpdated("0 B"))
                    dispatch(Message.ShowClearCacheDialog(false))
                } else {
                    // Could show error message
                }
            } catch (e: Exception) {
                // Could show error message
            }
        }
    }

    private fun obtainMenu(itemId: Int) {
        when (itemId) {
            0 -> publish(Label.OutPut(SettingsOutput.ChangeTheme))
            1 -> dispatch(Message.ShowClearCacheDialog(true))
            4 -> intentAction.openWebPage(SPORT_SAUCE_GROUP)
            5 -> intentAction.openWebPage(GIT_HUB_WEB_PAGE)
            7 -> publish(Label.OutPut(SettingsOutput.CheckUpdates))
        }
    }
}

private const val GIT_HUB_WEB_PAGE = "https://github.com/MarketTwits"
private const val SPORT_SAUCE_GROUP = "https://t.me/sportsauce"
