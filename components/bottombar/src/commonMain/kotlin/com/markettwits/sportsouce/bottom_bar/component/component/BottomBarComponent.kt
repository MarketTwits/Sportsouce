package com.markettwits.sportsouce.bottom_bar.component.component

import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomBarState
import kotlinx.coroutines.flow.StateFlow

interface BottomBarComponent {

    val state: StateFlow<BottomBarState>

    val listener: BottomBarVisibilityListener

    fun onClickTabBar(bottomBarConfiguration: BottomBarConfiguration)
}