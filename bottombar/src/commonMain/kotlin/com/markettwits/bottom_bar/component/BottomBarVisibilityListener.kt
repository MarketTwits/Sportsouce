package com.markettwits.bottom_bar.component

import com.markettwits.bottom_bar.components.defaultMenuItems
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface BottomBarVisibilityListener {
    fun show()
    fun hide()
}

interface BottomBarStorage : BottomBarVisibilityListener {
    val state: StateFlow<BottomBarState>
    fun onSelectTab(configuration: Configuration)
}

object BottomBarStorageImpl : BottomBarStorage {
    override val state: MutableStateFlow<BottomBarState> = MutableStateFlow(
        BottomBarState(
            showTopBar = true,
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