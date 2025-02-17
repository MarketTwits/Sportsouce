package com.markettwits.bottom_bar.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import com.markettwits.bottom_bar.model.BottomBarConfiguration
import com.markettwits.bottom_bar.model.BottomNavigationItem

internal fun defaultMenuItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Старты",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            bottomBarConfiguration = BottomBarConfiguration.Home,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Обзор",
            selectedIcon = Icons.Filled.Newspaper,
            unselectedIcon = Icons.Outlined.Newspaper,
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