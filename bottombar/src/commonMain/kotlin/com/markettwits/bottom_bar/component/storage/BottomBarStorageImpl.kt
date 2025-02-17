package com.markettwits.bottom_bar.component.storage

import com.markettwits.bottom_bar.components.defaultMenuItems
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.BottomBarConfiguration
import com.markettwits.settings.api.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update


data object BottomBarStorageImpl : BottomBarStorage {

    override val state: MutableStateFlow<BottomBarState> = MutableStateFlow(
        BottomBarState(
            showTopBar = true,
            showLabel = true,
            selectedTab = BottomBarConfiguration.Home,
            items = defaultMenuItems()
        )
    )

    override fun onSelectTab(bottomBarConfiguration: BottomBarConfiguration) {
        state.update {
            it.copy(selectedTab = bottomBarConfiguration)
        }
    }

    override fun show() {
        state.update {
            it.copy(showTopBar = true)
        }
    }

    override fun hide() {
        state.update {
            it.copy(showTopBar = false)
        }
    }
}