package com.markettwits.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.settings.internal.settings_menu.component.SettingsOutput
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Intent
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Label
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.State

interface SettingsStore : Store<Intent, State, Label> {
    data object State

    sealed interface Intent {
        data object GoBack : Intent
        data class OnClickItemMenu(val itemId: Int) : Intent
    }

    sealed interface Message

    sealed interface Label {
        data object GoBack : Label
        data class OutPut(val outPut: SettingsOutput) : Label
    }

}
