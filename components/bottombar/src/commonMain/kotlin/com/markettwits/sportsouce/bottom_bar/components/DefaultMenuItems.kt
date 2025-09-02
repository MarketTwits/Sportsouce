package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Timeline
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem

internal fun defaultMenuItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Старты",
            selectedIcon = Icons.Filled.Flag,
            unselectedIcon = Icons.Outlined.Flag,
            bottomBarConfiguration = BottomBarConfiguration.Home,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Обзор",
            selectedIcon = Icons.Filled.Timeline,
            unselectedIcon = Icons.Outlined.Timeline,
            hasNews = false,
            bottomBarConfiguration = BottomBarConfiguration.Review,
        ),
        BottomNavigationItem(
            title = "Профиль",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            hasNews = false,
            bottomBarConfiguration = BottomBarConfiguration.Profile,
        ),
    )
}