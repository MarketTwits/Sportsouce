package com.markettwits.settings.internal.appearance.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.settings.internal.appearance.components.BottomBarUi


interface AppearanceStore :
    Store<AppearanceStore.Intent, AppearanceStore.State, AppearanceStore.Label> {

    data class State(val items: List<BottomBarUi>)

    sealed interface Intent {
        data object OnClickGoBack : Intent
        data class OnClickItem(val item: BottomBarUi) : Intent
    }

    sealed interface Message {
        data class ThemeMenu(val item: List<BottomBarUi>) : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}
