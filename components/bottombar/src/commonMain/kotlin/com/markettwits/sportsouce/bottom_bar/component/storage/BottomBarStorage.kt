package com.markettwits.sportsouce.bottom_bar.component.storage

import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomBarState
import kotlinx.coroutines.flow.StateFlow

interface BottomBarStorage : BottomBarVisibilityListener {

    val state: StateFlow<BottomBarState>

    fun onSelectTab(bottomBarConfiguration: BottomBarConfiguration)

}

