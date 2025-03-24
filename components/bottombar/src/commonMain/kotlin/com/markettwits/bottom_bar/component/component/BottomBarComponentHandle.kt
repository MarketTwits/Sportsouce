package com.markettwits.bottom_bar.component.component

import com.markettwits.bottom_bar.model.BottomBarConfiguration

interface BottomBarComponentHandle {

    fun navigateTo(bottomBarConfiguration: BottomBarConfiguration)

    fun getActiveConfiguration(observer: (BottomBarConfiguration) -> Unit)

}