package com.markettwits.sportsouce.bottom_bar.model

import androidx.compose.runtime.Immutable

@Immutable
data class BottomBarState(
    val showTopBar: Boolean,
    val selectedTab: BottomBarConfiguration,
    val showLabel: Boolean,
    val items: List<BottomNavigationItem>
)