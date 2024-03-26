package com.markettwits.root.bottom_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.root.root.RootComponent

@Composable
internal fun BottomBar(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.configuration
    val items = defaultMenuItems()
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