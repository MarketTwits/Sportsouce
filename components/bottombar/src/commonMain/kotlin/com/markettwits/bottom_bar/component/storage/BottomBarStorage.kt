package com.markettwits.bottom_bar.component.storage

import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.BottomBarConfiguration
import kotlinx.coroutines.flow.StateFlow

interface BottomBarStorage : BottomBarVisibilityListener {

    val state: StateFlow<BottomBarState>

    fun onSelectTab(bottomBarConfiguration: BottomBarConfiguration)

}

