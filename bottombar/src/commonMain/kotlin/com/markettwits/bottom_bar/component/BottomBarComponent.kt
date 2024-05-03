package com.markettwits.bottom_bar.component

import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.Configuration
import kotlinx.coroutines.flow.StateFlow

interface BottomBarComponent {
    val state: StateFlow<BottomBarState>
    fun onClickTabBar(configuration: Configuration)
}