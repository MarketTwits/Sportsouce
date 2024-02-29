package com.markettwits.root.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.root.root.RootComponent

@Composable
fun BottomBar(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.configuration
    val items = menuItems()
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        items.forEach { item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                selected = activeComponent == item.configuration,
                onClick = {
                    component.navigate(item.configuration)
                },
                label = {
                    Text(text = item.title, color = MaterialTheme.colorScheme.tertiary)
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (activeComponent == item.configuration) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )
        }
    }

}

private fun menuItems(): List<BottomNavigationItem> {
    val items = listOf(
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
    return items
}

data class BottomNavigationItem(
    val title: String,
    val configuration: RootComponent.Configuration,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)