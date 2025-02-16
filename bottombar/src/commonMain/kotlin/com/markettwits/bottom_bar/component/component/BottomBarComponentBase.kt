package com.markettwits.bottom_bar.component.component

import com.arkivanov.decompose.ComponentContext
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.bottom_bar.component.storage.BottomBarStorage
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.BottomBarConfiguration
import kotlinx.coroutines.flow.StateFlow

class BottomBarComponentBase(
    private val componentContext: ComponentContext,
    private val navigationComponentHandle: BottomBarComponentHandle,
    private val bottomBarStorage: BottomBarStorage
) : BottomBarComponent, ComponentContext by componentContext {

    override val state: StateFlow<BottomBarState> = bottomBarStorage.state

    override fun onClickTabBar(bottomBarConfiguration: BottomBarConfiguration) {
        navigationComponentHandle.navigateTo(bottomBarConfiguration)
    }

    override val listener: BottomBarVisibilityListener = bottomBarStorage

    init {
        navigationComponentHandle.getActiveConfiguration { configuration ->
            bottomBarStorage.onSelectTab(configuration)
        }
    }
}


