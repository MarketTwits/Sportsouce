package com.markettwits.sportsouce.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.settings.internal.settings_menu.component.SettingsOutput
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore.*
import com.markettwits.version.ApplicationVersion

interface SettingsStore : Store<Intent, State, Label> {
    data class State(
        val version: ApplicationVersion?,
        val cacheSize: String = "Загрузка...",
        val showClearCacheDialog: Boolean = false,
    )

    sealed interface Intent {
        data object GoBack : Intent
        data class OnClickItemMenu(val itemId: Int) : Intent
        data object ShowClearCacheDialog : Intent
        data object HideClearCacheDialog : Intent
        data object ClearCache : Intent
    }

    sealed interface Message {
        data class CurrentVersion(val version: ApplicationVersion) : Message
        data class CacheSizeUpdated(val size: String) : Message
        data class ShowClearCacheDialog(val show: Boolean) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class OutPut(val outPut: SettingsOutput) : Label
    }

}
