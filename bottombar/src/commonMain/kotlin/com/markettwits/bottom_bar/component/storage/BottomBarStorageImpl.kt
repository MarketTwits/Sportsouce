package com.markettwits.bottom_bar.component.storage

import com.markettwits.bottom_bar.components.defaultMenuItems
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object BottomBarStorageImpl : BottomBarStorage {

    override val state: MutableStateFlow<BottomBarState> = MutableStateFlow(
        BottomBarState(
            showTopBar = true,
            showLabel = false,
            selectedTab = Configuration.Home,
            items = defaultMenuItems()
        )
    )

    override fun onSelectTab(configuration: Configuration) {
        state.update {
            it.copy(selectedTab = configuration)
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