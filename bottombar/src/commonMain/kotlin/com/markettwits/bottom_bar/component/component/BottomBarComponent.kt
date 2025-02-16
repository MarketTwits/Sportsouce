package com.markettwits.bottom_bar.component.component

import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.BottomBarConfiguration
import kotlinx.coroutines.flow.StateFlow

interface BottomBarComponent {

    val state: StateFlow<BottomBarState>

    val listener: BottomBarVisibilityListener

    fun onClickTabBar(bottomBarConfiguration: BottomBarConfiguration)
}