package com.markettwits.sportsouce.bottom_bar.component.storage

import com.markettwits.sportsouce.bottom_bar.components.defaultMenuItems
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomBarState
import kotlinx.coroutines.flow.MutableStateFlow
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