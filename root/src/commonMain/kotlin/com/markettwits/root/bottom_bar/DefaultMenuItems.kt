package com.markettwits.root.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import com.markettwits.root.root.RootComponent

internal fun defaultMenuItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Старты",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            configuration = RootComponent.Configuration.Starts,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Обзор",
            selectedIcon = Icons.Filled.Newspaper,
            unselectedIcon = Icons.Outlined.Newspaper,
            hasNews = false,
            configuration = RootComponent.Configuration.Review,
        ),
        BottomNavigationItem(
            title = "Профиль",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            hasNews = false,
            configuration = RootComponent.Configuration.Profile,
        ),
    )
}