package com.markettwits.root.bottom_bar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.root.root.MockRootComponent
import com.markettwits.root.root.RootComponent

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(MockRootComponent())
}

@Composable
fun BottomBar(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.configuration
    val items = menuItems()

    Column {
        HorizontalDivider()
        NavigationBar(containerColor = Color.White) {
            items.forEach { item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = SportSouceColor.SportSouceLighBlue.copy(alpha = 0.3f)
                    ),
                    selected = activeComponent == item.configuration,
                    onClick = {
                        component.navigate(item.configuration)
                    },
                    label = {
                        Text(text = item.title, color = Color(61, 82, 139))
                    },
                    alwaysShowLabel = true,
                    icon = {
                        Icon(
                            imageVector = if (activeComponent == item.configuration) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = Color(61, 82, 139)
                        )
                    }
                )
            }
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
            title = "Новости",
            selectedIcon = Icons.Filled.Newspaper,
            unselectedIcon = Icons.Outlined.Newspaper,
            hasNews = false,
            configuration = RootComponent.Configuration.News,
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