package com.markettwits.bottom_bar.model

data class BottomBarState(
    val showTopBar: Boolean,
    val selectedTab: Configuration,
    val items: List<BottomNavigationItem>
)