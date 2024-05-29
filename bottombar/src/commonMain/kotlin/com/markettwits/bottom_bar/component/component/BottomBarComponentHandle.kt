package com.markettwits.bottom_bar.component.component

import com.markettwits.bottom_bar.model.Configuration

interface BottomBarComponentHandle {
    fun navigateTo(configuration: Configuration)
    fun getActiveConfiguration(observer: (Configuration) -> Unit)
}