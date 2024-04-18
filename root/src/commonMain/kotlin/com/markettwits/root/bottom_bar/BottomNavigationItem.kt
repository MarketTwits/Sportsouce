package com.markettwits.root.bottom_bar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.root.root.RootComponent

@Immutable
internal data class BottomNavigationItem(
    val title: String,
    val configuration: RootComponent.Configuration,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)