package com.markettwits.sportsouce.bottom_bar.component.component

import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration

interface BottomBarComponentHandle {

    fun navigateTo(bottomBarConfiguration: BottomBarConfiguration)

    fun getActiveConfiguration(observer: (BottomBarConfiguration) -> Unit)

}